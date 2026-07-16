package com.owlen.app.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owlen.app.data.log.SessionLogger
import com.owlen.app.data.log.SessionSummary
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.presentation.ui.SessionEventItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionLogger: SessionLogger
) : ViewModel() {

    private val _recentSessions = MutableStateFlow<List<SessionSummary>>(emptyList())
    val recentSessions: StateFlow<List<SessionSummary>> = _recentSessions.asStateFlow()

    private val _latestSessionEvents = MutableStateFlow<List<SessionEventItem>>(emptyList())
    val latestSessionEvents: StateFlow<List<SessionEventItem>> = _latestSessionEvents.asStateFlow()

    private val _latestSummary = MutableStateFlow<SessionSummary?>(null)
    val latestSummary: StateFlow<SessionSummary?> = _latestSummary.asStateFlow()

    /** Spec: suggest lowering sensitivity when >3 maskings scored under 40 (likely false positives). */
    private val _adjustSensitivitySuggested = MutableStateFlow(false)
    val adjustSensitivitySuggested: StateFlow<Boolean> = _adjustSensitivitySuggested.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val summaries = sessionLogger.getRecentSessions()
            _recentSessions.value = summaries
            val latest = summaries.firstOrNull()
            _latestSummary.value = latest
            _latestSessionEvents.value = latest?.let { summary ->
                sessionLogger.getSessionDetails(summary.sessionId)?.events?.map { log ->
                    SessionEventItem(
                        timestampMs = log.timestampMs,
                        eventClass = runCatching { EventClass.valueOf(log.eventClass) }
                            .getOrDefault(EventClass.UNKNOWN),
                        score = log.disturbanceScore,
                        wasMasked = log.action == "MASKED",
                        maskingSound = log.maskingSound?.let { name ->
                            runCatching { MaskingSound.valueOf(name) }.getOrNull()
                        },
                        maskingVolume = log.maskingVolume ?: 0f,
                        durationMs = log.durationMs ?: 0L
                    )
                }?.sortedByDescending { it.timestampMs } ?: emptyList()
            } ?: emptyList()
            _adjustSensitivitySuggested.value =
                _latestSessionEvents.value.count { it.wasMasked && it.score < 40 } > 3
        }
    }
}
