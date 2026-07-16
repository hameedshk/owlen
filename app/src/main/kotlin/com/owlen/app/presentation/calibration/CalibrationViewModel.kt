package com.owlen.app.presentation.calibration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owlen.app.data.audio.AudioCapture
import com.owlen.app.data.ml.RmsCalculator
import com.owlen.app.data.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * Measures the room's ambient sound level during calibration and persists it
 * as the scorer's soundLevelFloor, so disturbance scores are relative to the
 * user's actual environment.
 */
@HiltViewModel
class CalibrationViewModel @Inject constructor(
    private val audioCapture: AudioCapture,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    companion object {
        // Below this estimated SPL the mic is likely covered or blocked
        const val OBSTRUCTION_DB_THRESHOLD = 20f

        // ~10 seconds of consecutive sub-threshold windows (each window is ~0.975s)
        const val OBSTRUCTION_WINDOW_COUNT = 10
    }

    private val _currentDbLevel = MutableStateFlow(0f)
    val currentDbLevel: StateFlow<Float> = _currentDbLevel.asStateFlow()

    private val _obstructed = MutableStateFlow(false)
    val obstructed: StateFlow<Boolean> = _obstructed.asStateFlow()

    private val samples = mutableListOf<Float>()
    private var consecutiveLowWindows = 0
    private var measureJob: Job? = null

    fun startMeasuring() {
        if (measureJob?.isActive == true) return
        samples.clear()
        consecutiveLowWindows = 0
        _obstructed.value = false
        audioCapture.start(viewModelScope)
        measureJob = viewModelScope.launch {
            audioCapture.audioFlow.collect { window ->
                val db = RmsCalculator.calculate(window)
                _currentDbLevel.value = db

                if (_obstructed.value) return@collect

                if (db < OBSTRUCTION_DB_THRESHOLD) {
                    consecutiveLowWindows++
                    if (consecutiveLowWindows >= OBSTRUCTION_WINDOW_COUNT) {
                        _obstructed.value = true
                    }
                } else {
                    consecutiveLowWindows = 0
                    samples.add(db)
                }
            }
        }
    }

    /** Restart measurement after the user repositions the device. */
    fun retry() {
        stopCapture()
        startMeasuring()
    }

    fun finishMeasuring() {
        stopCapture()
        val floor = if (samples.isNotEmpty()) {
            samples.sorted()[samples.size / 2]  // median is robust to spikes
        } else {
            null
        }
        persistResult(floor)
    }

    /** Skip: keep the configured static floor but still mark today calibrated. */
    fun skip() {
        stopCapture()
        persistResult(floor = null)
    }

    private fun persistResult(floor: Float?) {
        val today = LocalDate.now().toEpochDay()
        // NonCancellable: the screen navigates away immediately, and these two
        // small DataStore writes must survive the ViewModel being cleared
        viewModelScope.launch(NonCancellable) {
            if (floor != null) {
                settingsRepository.updateSoundLevelFloor(floor.coerceIn(20f, 70f))
            }
            settingsRepository.updateLastCalibrationDay(today)
        }
    }

    private fun stopCapture() {
        measureJob?.cancel()
        measureJob = null
        audioCapture.stop()
    }

    override fun onCleared() {
        stopCapture()
        super.onCleared()
    }
}
