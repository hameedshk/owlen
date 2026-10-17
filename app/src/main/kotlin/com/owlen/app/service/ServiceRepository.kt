package com.owlen.app.service

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceRepository @Inject constructor() {
    private val _state = MutableStateFlow<ServiceState>(ServiceState.Idle)
    val state: StateFlow<ServiceState> = _state.asStateFlow()

    private val _sessionStartTime = MutableStateFlow<Long?>(null)
    val sessionStartTime: StateFlow<Long?> = _sessionStartTime.asStateFlow()

    private val _lastDetection = MutableStateFlow<Pair<DetectedEvent, DisturbanceResult>?>(null)
    val lastDetection: StateFlow<Pair<DetectedEvent, DisturbanceResult>?> = _lastDetection.asStateFlow()

    fun updateState(newState: ServiceState) {
        _state.value = newState
    }

    fun updateSessionStartTime(time: Long?) {
        _sessionStartTime.value = time
    }

    fun updateLastDetection(event: DetectedEvent, result: DisturbanceResult) {
        _lastDetection.value = event to result
    }
}
