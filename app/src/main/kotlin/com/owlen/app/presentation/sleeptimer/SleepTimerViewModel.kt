package com.owlen.app.presentation.sleeptimer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owlen.app.data.settings.SettingsRepository
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.SleepSettings
import com.owlen.app.domain.model.SleepTimerConfig
import com.owlen.app.domain.model.SleepTimerSchedule
import com.owlen.app.service.ServiceRepository
import com.owlen.app.service.ServiceState
import com.owlen.app.service.SleepProtectionService
import com.owlen.app.service.SleepTimerScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepTimerViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val serviceRepository: ServiceRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val scheduler = SleepTimerScheduler(appContext)

    // Eagerly shared (not WhileSubscribed): this mirrors a foreground service that keeps
    // running and emitting regardless of whether the screen is currently collecting, so a
    // lazily-(re)started subscription must never be the thing deciding whether Preview/Timer
    // UI reflects live playback.
    val serviceState: StateFlow<ServiceState> = serviceRepository.state
        .stateIn(viewModelScope, SharingStarted.Eagerly, serviceRepository.state.value)

    val settings: StateFlow<SleepSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SleepSettings.default())

    val schedule: StateFlow<SleepTimerSchedule> = settingsRepository.schedule
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SleepTimerSchedule())

    val timerConfig: StateFlow<SleepTimerConfig> = settingsRepository.timerConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SleepTimerConfig())

    val isProtectionRunning: StateFlow<Boolean> = serviceState
        .map { it is ServiceState.Monitoring || it is ServiceState.MaskingActive || it is ServiceState.SafetyAlertActive }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    /** A countdown belongs on screen only for a real timer — a preview is not a timer. */
    val isTimerRunning: StateFlow<Boolean> = serviceState
        .map { state ->
            state is ServiceState.TimedPlaybackActive &&
                state.mode == SleepProtectionService.PlaybackMode.TIMER
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val isPreviewPlaying: StateFlow<Boolean> = serviceState
        .map { state ->
            state is ServiceState.TimedPlaybackActive &&
                state.mode == SleepProtectionService.PlaybackMode.PREVIEW
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _hasExactAlarmPermission = MutableStateFlow(scheduler.hasExactAlarmPermission())

    /** Revocable at any time on Android 12+, so screens re-check this on resume. */
    val hasExactAlarmPermission: StateFlow<Boolean> = _hasExactAlarmPermission.asStateFlow()

    fun refreshExactAlarmPermission() {
        _hasExactAlarmPermission.value = scheduler.hasExactAlarmPermission()
    }

    fun startTimer(
        sound: MaskingSound,
        durationMs: Long,
        targetVolume: Float,
        fadeOutMs: Long = 0L
    ) {
        SleepProtectionService.startTimer(
            context = appContext,
            sound = sound,
            durationMs = durationMs,
            targetVolume = targetVolume,
            mode = SleepProtectionService.PlaybackMode.TIMER,
            fadeMs = fadeOutMs
        )
    }

    fun stopTimer() {
        SleepProtectionService.stopTimer(appContext)
    }

    fun startPreview(sound: MaskingSound, durationMs: Long = 5_000L, volume: Float = 0.5f) {
        SleepProtectionService.startPreview(appContext, sound, durationMs, volume)
    }

    fun stopPreview() {
        SleepProtectionService.stopPreview(appContext)
    }

    fun saveTimerConfig(config: SleepTimerConfig) {
        viewModelScope.launch {
            settingsRepository.updateTimerConfig(config)
        }
    }

    fun saveSchedule(schedule: SleepTimerSchedule) {
        viewModelScope.launch {
            settingsRepository.updateSchedule(schedule)
            if (schedule.enabled) {
                scheduler.scheduleNext(schedule)
            } else {
                // Cancelling the alarm only stops future starts. If the window is
                // open right now the sound is already playing, and leaving it to
                // run would ignore the switch the user just turned off.
                scheduler.cancel()
                SleepProtectionService.stopTimer(appContext)
            }
            refreshExactAlarmPermission()
        }
    }
}
