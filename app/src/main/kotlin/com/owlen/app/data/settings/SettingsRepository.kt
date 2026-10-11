package com.owlen.app.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.Sensitivity
import com.owlen.app.domain.model.SleepSettings
import com.owlen.app.domain.model.SleepTimerConfig
import com.owlen.app.domain.model.SleepTimerSchedule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sleep_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val SLEEP_WINDOW_START = intPreferencesKey("sleep_window_start")
        private val SLEEP_WINDOW_END = intPreferencesKey("sleep_window_end")
        private val WAKE_TIME_HOUR = intPreferencesKey("wake_time_hour")
        private val WAKE_TIME_MINUTE = intPreferencesKey("wake_time_minute")
        private val SENSITIVITY = stringPreferencesKey("sensitivity")
        private val MAX_VOLUME = floatPreferencesKey("max_volume")
        private val PREFERRED_SOUND = stringPreferencesKey("preferred_sound")
        private val AUTO_STOP_DURATION = intPreferencesKey("auto_stop_duration_ms")
        private val RAIN_BEHAVIOUR = booleanPreferencesKey("rain_behaviour_enabled")
        private val SOUND_LEVEL_FLOOR = floatPreferencesKey("sound_level_floor")
        private val LAST_CALIBRATION_DAY = longPreferencesKey("last_calibration_epoch_day")
        private val SCHEDULE_ENABLED = booleanPreferencesKey("schedule_enabled")
        private val SCHEDULE_START_HOUR = intPreferencesKey("schedule_start_hour")
        private val SCHEDULE_START_MINUTE = intPreferencesKey("schedule_start_minute")
        private val SCHEDULE_END_HOUR = intPreferencesKey("schedule_end_hour")
        private val SCHEDULE_END_MINUTE = intPreferencesKey("schedule_end_minute")
        private val SCHEDULE_SOUND = stringPreferencesKey("schedule_sound")
        private val SCHEDULE_FADE_IN_MINUTES = intPreferencesKey("schedule_fade_in_minutes")
        private val SCHEDULE_MAX_VOLUME = floatPreferencesKey("schedule_max_volume")
        private val TIMER_DURATION_MINUTES = intPreferencesKey("timer_duration_minutes")
        private val TIMER_FADE_OUT_MINUTES = intPreferencesKey("timer_fade_out_minutes")
        private val TIMER_SOUND = stringPreferencesKey("timer_sound")
    }

    private fun preferencesToSettings(pref: Preferences): SleepSettings {
        return SleepSettings(
            sleepWindowStartHour = pref[SLEEP_WINDOW_START] ?: SleepSettings.default().sleepWindowStartHour,
            sleepWindowEndHour = pref[SLEEP_WINDOW_END] ?: SleepSettings.default().sleepWindowEndHour,
            wakeTimeHour = pref[WAKE_TIME_HOUR] ?: SleepSettings.default().wakeTimeHour,
            wakeTimeMinute = pref[WAKE_TIME_MINUTE] ?: SleepSettings.default().wakeTimeMinute,
            sensitivity = pref[SENSITIVITY]?.let {
                try {
                    Sensitivity.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    SleepSettings.default().sensitivity
                }
            } ?: SleepSettings.default().sensitivity,
            maxVolume = pref[MAX_VOLUME] ?: SleepSettings.default().maxVolume,
            preferredSound = pref[PREFERRED_SOUND]?.let {
                try {
                    MaskingSound.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    SleepSettings.default().preferredSound
                }
            } ?: SleepSettings.default().preferredSound,
            autoStopDurationMs = pref[AUTO_STOP_DURATION]?.toLong() ?: SleepSettings.default().autoStopDurationMs,
            rainBehaviourEnabled = pref[RAIN_BEHAVIOUR] ?: SleepSettings.default().rainBehaviourEnabled,
            soundLevelFloor = pref[SOUND_LEVEL_FLOOR] ?: SleepSettings.default().soundLevelFloor
        )
    }

    private fun preferencesToSchedule(pref: Preferences): SleepTimerSchedule {
        return SleepTimerSchedule(
            enabled = pref[SCHEDULE_ENABLED] ?: false,
            startHour = pref[SCHEDULE_START_HOUR] ?: 6,
            startMinute = pref[SCHEDULE_START_MINUTE] ?: 0,
            endHour = pref[SCHEDULE_END_HOUR] ?: 8,
            endMinute = pref[SCHEDULE_END_MINUTE] ?: 0,
            sound = pref[SCHEDULE_SOUND]?.let {
                try {
                    MaskingSound.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    MaskingSound.RAIN
                }
            } ?: MaskingSound.RAIN,
            fadeInMinutes = pref[SCHEDULE_FADE_IN_MINUTES] ?: 10,
            maxVolume = pref[SCHEDULE_MAX_VOLUME] ?: 0.5f
        )
    }

    private fun preferencesToTimerConfig(pref: Preferences): SleepTimerConfig {
        return SleepTimerConfig(
            sound = pref[TIMER_SOUND]?.let {
                try {
                    MaskingSound.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    MaskingSound.RAIN
                }
            } ?: MaskingSound.RAIN,
            durationMinutes = pref[TIMER_DURATION_MINUTES] ?: 30,
            fadeOutMinutes = pref[TIMER_FADE_OUT_MINUTES] ?: 10,
            maxVolume = 0.7f
        )
    }

    /** Epoch day (days since 1970-01-01) of the most recent calibration; -1 if never run. */
    val lastCalibrationDay: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[LAST_CALIBRATION_DAY] ?: -1L
    }

    suspend fun updateLastCalibrationDay(epochDay: Long) {
        context.dataStore.edit { preferences ->
            preferences[LAST_CALIBRATION_DAY] = epochDay
        }
    }

    val settings: Flow<SleepSettings> = context.dataStore.data.map { preferences ->
        preferencesToSettings(preferences)
    }

    suspend fun updateSleepWindow(startHour: Int, endHour: Int) {
        context.dataStore.edit { preferences ->
            preferences[SLEEP_WINDOW_START] = startHour
            preferences[SLEEP_WINDOW_END] = endHour
        }
    }

    suspend fun updateWakeTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[WAKE_TIME_HOUR] = hour
            preferences[WAKE_TIME_MINUTE] = minute
        }
    }

    suspend fun updateSensitivity(sensitivity: Sensitivity) {
        context.dataStore.edit { preferences ->
            preferences[SENSITIVITY] = sensitivity.name
        }
    }

    suspend fun updateMaxVolume(volume: Float) {
        context.dataStore.edit { preferences ->
            preferences[MAX_VOLUME] = volume
        }
    }

    suspend fun updatePreferredSound(sound: MaskingSound) {
        context.dataStore.edit { preferences ->
            preferences[PREFERRED_SOUND] = sound.name
        }
    }

    suspend fun updateAutoStopDuration(durationMs: Long) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_STOP_DURATION] = durationMs.toInt()
        }
    }

    suspend fun updateRainBehaviour(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[RAIN_BEHAVIOUR] = enabled
        }
    }

    suspend fun updateSoundLevelFloor(floor: Float) {
        context.dataStore.edit { preferences ->
            preferences[SOUND_LEVEL_FLOOR] = floor
        }
    }

    suspend fun getSettings(): SleepSettings {
        val pref = context.dataStore.data.first()
        return preferencesToSettings(pref)
    }

    val schedule: Flow<SleepTimerSchedule> = context.dataStore.data.map { pref ->
        preferencesToSchedule(pref)
    }

    suspend fun getSchedule(): SleepTimerSchedule {
        val pref = context.dataStore.data.first()
        return preferencesToSchedule(pref)
    }

    suspend fun updateSchedule(schedule: SleepTimerSchedule) {
        context.dataStore.edit { preferences ->
            preferences[SCHEDULE_ENABLED] = schedule.enabled
            preferences[SCHEDULE_START_HOUR] = schedule.startHour
            preferences[SCHEDULE_START_MINUTE] = schedule.startMinute
            preferences[SCHEDULE_END_HOUR] = schedule.endHour
            preferences[SCHEDULE_END_MINUTE] = schedule.endMinute
            preferences[SCHEDULE_SOUND] = schedule.sound.name
            preferences[SCHEDULE_FADE_IN_MINUTES] = schedule.fadeInMinutes
            preferences[SCHEDULE_MAX_VOLUME] = schedule.maxVolume
        }
    }

    val timerConfig: Flow<SleepTimerConfig> = context.dataStore.data.map { pref ->
        preferencesToTimerConfig(pref)
    }

    suspend fun getTimerConfig(): SleepTimerConfig {
        val pref = context.dataStore.data.first()
        return preferencesToTimerConfig(pref)
    }

    suspend fun updateTimerConfig(config: SleepTimerConfig) {
        context.dataStore.edit { preferences ->
            preferences[TIMER_SOUND] = config.sound.name
            preferences[TIMER_DURATION_MINUTES] = config.durationMinutes
            preferences[TIMER_FADE_OUT_MINUTES] = config.fadeOutMinutes
        }
    }
}
