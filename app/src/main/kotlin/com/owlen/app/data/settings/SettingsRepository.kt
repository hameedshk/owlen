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
        SleepSettings(
            sleepWindowStartHour = preferences[SLEEP_WINDOW_START] ?: SleepSettings.default().sleepWindowStartHour,
            sleepWindowEndHour = preferences[SLEEP_WINDOW_END] ?: SleepSettings.default().sleepWindowEndHour,
            wakeTimeHour = preferences[WAKE_TIME_HOUR] ?: SleepSettings.default().wakeTimeHour,
            wakeTimeMinute = preferences[WAKE_TIME_MINUTE] ?: SleepSettings.default().wakeTimeMinute,
            sensitivity = preferences[SENSITIVITY]?.let {
                try {
                    Sensitivity.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    SleepSettings.default().sensitivity
                }
            } ?: SleepSettings.default().sensitivity,
            maxVolume = preferences[MAX_VOLUME] ?: SleepSettings.default().maxVolume,
            preferredSound = preferences[PREFERRED_SOUND]?.let {
                try {
                    MaskingSound.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    SleepSettings.default().preferredSound
                }
            } ?: SleepSettings.default().preferredSound,
            autoStopDurationMs = preferences[AUTO_STOP_DURATION]?.toLong() ?: SleepSettings.default().autoStopDurationMs,
            rainBehaviourEnabled = preferences[RAIN_BEHAVIOUR] ?: SleepSettings.default().rainBehaviourEnabled,
            soundLevelFloor = preferences[SOUND_LEVEL_FLOOR] ?: SleepSettings.default().soundLevelFloor
        )
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
}
