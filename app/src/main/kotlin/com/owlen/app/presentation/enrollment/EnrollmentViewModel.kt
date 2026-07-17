package com.owlen.app.presentation.enrollment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owlen.app.data.audio.AudioCapture
import com.owlen.app.data.ml.FeatureExtractor
import com.owlen.app.data.ml.RmsCalculator
import com.owlen.app.data.prototypes.SoundPrototypeRepository
import com.owlen.app.domain.matcher.PrototypeBuilder
import com.owlen.app.service.ServiceRepository
import com.owlen.app.service.ServiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * Guides the user through enrolling a custom disturbance: three short takes of
 * the same sound, each reduced to the embedding of its loudest window, then
 * combined into a [com.owlen.app.domain.model.SoundPrototype] by
 * [PrototypeBuilder]. Orchestration only — all matching/threshold logic lives
 * in the domain layer.
 */
@HiltViewModel
class EnrollmentViewModel @Inject constructor(
    private val audioCapture: AudioCapture,
    private val featureExtractor: FeatureExtractor,
    private val prototypeBuilder: PrototypeBuilder,
    private val prototypeRepository: SoundPrototypeRepository,
    serviceRepository: ServiceRepository
) : ViewModel() {

    enum class Phase { IDLE, RECORDING, PROCESSING, INCONSISTENT, SAVED }

    data class UiState(
        val name: String = "",
        val takesCompleted: Int = 0,
        val phase: Phase = Phase.IDLE,
        val liveDb: Float = 0f
    )

    companion object {
        const val TAKES_REQUIRED = 3

        // ~3 seconds of audio per take (each window is ~0.975s)
        const val WINDOWS_PER_TAKE = 3
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /** Enrollment must not contend with the sleep service for the shared mic. */
    val isMonitoringActive: StateFlow<Boolean> = serviceRepository.state
        .map { it !is ServiceState.Idle && it !is ServiceState.Interrupted }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val takeEmbeddings = mutableListOf<FloatArray>()
    private var recordJob: Job? = null

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun recordTake() {
        if (recordJob?.isActive == true) return
        if (isMonitoringActive.value) return
        if (_uiState.value.phase == Phase.PROCESSING || _uiState.value.phase == Phase.SAVED) return

        _uiState.update { it.copy(phase = Phase.RECORDING) }
        audioCapture.start(viewModelScope)
        recordJob = viewModelScope.launch {
            val candidates = mutableListOf<Pair<Float, FloatArray>>()
            audioCapture.audioFlow.take(WINDOWS_PER_TAKE).collect { window ->
                val db = RmsCalculator.calculate(window)
                _uiState.update { it.copy(liveDb = db) }
                val extraction = featureExtractor.extract(window)
                candidates.add(db to extraction.embedding)
            }
            audioCapture.stop()

            takeEmbeddings.add(prototypeBuilder.selectBest(candidates))
            if (takeEmbeddings.size >= TAKES_REQUIRED) {
                finishEnrollment()
            } else {
                _uiState.update {
                    it.copy(takesCompleted = takeEmbeddings.size, phase = Phase.IDLE)
                }
            }
        }
    }

    private suspend fun finishEnrollment() {
        _uiState.update { it.copy(takesCompleted = TAKES_REQUIRED, phase = Phase.PROCESSING) }
        val name = _uiState.value.name.trim().ifBlank { "Custom Sound" }
        val result = prototypeBuilder.build(
            id = UUID.randomUUID().toString(),
            name = name,
            embeddings = takeEmbeddings.toList(),
            nowMs = System.currentTimeMillis()
        )
        when (result) {
            is PrototypeBuilder.BuildResult.Success -> {
                prototypeRepository.add(result.prototype)
                _uiState.update { it.copy(phase = Phase.SAVED) }
            }

            is PrototypeBuilder.BuildResult.TooInconsistent -> {
                _uiState.update { it.copy(phase = Phase.INCONSISTENT) }
            }
        }
    }

    /** Start over after inconsistent takes (or to enroll another sound). */
    fun restart() {
        recordJob?.cancel()
        recordJob = null
        audioCapture.stop()
        takeEmbeddings.clear()
        _uiState.update { UiState(name = it.name) }
    }

    override fun onCleared() {
        recordJob?.cancel()
        audioCapture.stop()
        super.onCleared()
    }
}
