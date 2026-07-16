package com.owlen.app.presentation.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J.\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\rJ\u000e\u0010\"\u001a\u00020\u000b2\u0006\u0010#\u001a\u00020\u0019J\u0016\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020\r2\u0006\u0010&\u001a\u00020\rR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/owlen/app/presentation/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsRepository", "Lcom/owlen/app/data/settings/SettingsRepository;", "(Lcom/owlen/app/data/settings/SettingsRepository;)V", "settings", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/owlen/app/domain/model/SleepSettings;", "getSettings", "()Lkotlinx/coroutines/flow/StateFlow;", "saveSetup", "", "sleepHour", "", "wakeHour", "wakeMinute", "sound", "Lcom/owlen/app/domain/model/MaskingSound;", "sensitivity", "Lcom/owlen/app/domain/model/Sensitivity;", "updateAutoStopDuration", "durationMs", "", "updateMaxVolume", "volume", "", "updatePreferredSound", "updateRainBehaviour", "enabled", "", "updateSensitivity", "updateSleepWindow", "startHour", "endHour", "updateSoundLevelFloor", "floor", "updateWakeTime", "hour", "minute", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.data.settings.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.owlen.app.domain.model.SleepSettings> settings = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.settings.SettingsRepository settingsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.owlen.app.domain.model.SleepSettings> getSettings() {
        return null;
    }
    
    public final void updateSleepWindow(int startHour, int endHour) {
    }
    
    public final void updateWakeTime(int hour, int minute) {
    }
    
    public final void updateSensitivity(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.Sensitivity sensitivity) {
    }
    
    public final void updateMaxVolume(float volume) {
    }
    
    public final void updatePreferredSound(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound sound) {
    }
    
    public final void updateAutoStopDuration(long durationMs) {
    }
    
    public final void updateSoundLevelFloor(float floor) {
    }
    
    public final void updateRainBehaviour(boolean enabled) {
    }
    
    public final void saveSetup(int sleepHour, int wakeHour, int wakeMinute, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound sound, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.Sensitivity sensitivity) {
    }
}