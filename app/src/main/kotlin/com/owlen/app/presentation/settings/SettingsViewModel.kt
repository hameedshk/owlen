package com.owlen.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owlen.app.data.settings.SettingsRepository
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.domain.model.SleepSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<SleepSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SleepSettings.default())

    fun updateSleepWindow(startHour: Int, endHour: Int) {
        viewModelScope.launch { settingsRepository.updateSleepWindow(startHour, endHour) }
    }

    fun updateWakeTime(hour: Int, minute: Int) {
        viewModelScope.launch { settingsRepository.updateWakeTime(hour, minute) }
    }

    fun updateSensitivity(sensitivity: Sensitivity) {
        viewModelScope.launch { settingsRepository.updateSensitivity(sensitivity) }
    }

    fun updateMaxVolume(volume: Float) {
        viewModelScope.launch { settingsRepository.updateMaxVolume(volume) }
    }

    fun updatePreferredSound(sound: MaskingSound) {
        viewModelScope.launch { settingsRepository.updatePreferredSound(sound) }
    }

    fun updateAutoStopDuration(durationMs: Long) {
        viewModelScope.launch { settingsRepository.updateAutoStopDuration(durationMs) }
    }

    fun updateSoundLevelFloor(floor: Float) {
        viewModelScope.launch { settingsRepository.updateSoundLevelFloor(floor) }
    }

    fun updateRainBehaviour(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateRainBehaviour(enabled) }
    }

    fun saveSetup(
        sleepHour: Int,
        wakeHour: Int,
        wakeMinute: Int,
        sound: MaskingSound,
        sensitivity: Sensitivity
    ) {
        viewModelScope.launch {
            settingsRepository.updateSleepWindow(sleepHour, wakeHour)
            settingsRepository.updateWakeTime(wakeHour, wakeMinute)
            settingsRepository.updatePreferredSound(sound)
            settingsRepository.updateSensitivity(sensitivity)
        }
    }
}
