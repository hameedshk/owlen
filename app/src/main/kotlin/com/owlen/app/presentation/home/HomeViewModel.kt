package com.owlen.app.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owlen.app.data.log.SessionLogger
import com.owlen.app.data.settings.SettingsRepository
import com.owlen.app.domain.model.SleepSettings
import com.owlen.app.service.ServiceRepository
import com.owlen.app.service.ServiceState
import com.owlen.app.service.SleepProtectionService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class InterruptionInfo(val sessionId: String, val interruptedAtMs: Long)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val serviceRepository: ServiceRepository,
    private val sessionLogger: SessionLogger,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val _interruption = MutableStateFlow<InterruptionInfo?>(null)

    /** Non-null when the last session ended with a system kill the user hasn't reviewed. */
    val interruption: StateFlow<InterruptionInfo?> = _interruption.asStateFlow()

    init {
        viewModelScope.launch {
            serviceRepository.state.collect { state ->
                if (state is ServiceState.Idle || state is ServiceState.Interrupted) {
                    val found = sessionLogger.findInterruptedSession()
                    _interruption.value = found?.let {
                        InterruptionInfo(it.sessionId, it.lastActivityMs)
                    }
                } else {
                    _interruption.value = null
                }
            }
        }
    }

    fun dismissInterruption() {
        val info = _interruption.value ?: return
        _interruption.value = null
        viewModelScope.launch {
            sessionLogger.closeSession(info.sessionId, info.interruptedAtMs)
        }
        if (serviceRepository.state.value is ServiceState.Interrupted) {
            serviceRepository.updateState(ServiceState.Idle)
        }
    }

    private val lastCalibrationDay: StateFlow<Long> = settingsRepository.lastCalibrationDay
        .stateIn(viewModelScope, SharingStarted.Eagerly, -1L)

    /** True when calibration has not run yet today (spec: recalibrate daily). */
    fun needsCalibrationToday(): Boolean =
        lastCalibrationDay.value != LocalDate.now().toEpochDay()

    val serviceState: StateFlow<ServiceState> = serviceRepository.state

    val sessionStartTime: StateFlow<Long?> = serviceRepository.sessionStartTime

    val lastDetection = serviceRepository.lastDetection

    val settings: StateFlow<SleepSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SleepSettings.default())

    val isProtectionActive: StateFlow<Boolean> = serviceRepository.state
        .map { it.isActive() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setProtectionEnabled(enabled: Boolean) {
        if (enabled) {
            SleepProtectionService.start(appContext)
        } else {
            SleepProtectionService.stop(appContext)
        }
    }

    fun acknowledgeSafetyAlert() {
        if (serviceRepository.state.value is ServiceState.SafetyAlertActive) {
            serviceRepository.updateState(ServiceState.Monitoring)
        }
    }

    private fun ServiceState.isActive(): Boolean = when (this) {
        ServiceState.Idle, is ServiceState.Interrupted -> false
        ServiceState.Monitoring, is ServiceState.MaskingActive, is ServiceState.SafetyAlertActive -> true
    }
}
