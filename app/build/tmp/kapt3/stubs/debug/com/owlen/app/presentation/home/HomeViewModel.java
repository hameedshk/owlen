package com.owlen.app.presentation.home;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B)\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0006\u0010#\u001a\u00020$J\u0006\u0010%\u001a\u00020$J\u0006\u0010&\u001a\u00020\u0013J\u000e\u0010\'\u001a\u00020$2\u0006\u0010(\u001a\u00020\u0013J\f\u0010)\u001a\u00020\u0013*\u00020\u001cH\u0002R\u0016\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R%\u0010\u0016\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u00170\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0011R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0011R\u0017\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0011\u00a8\u0006*"}, d2 = {"Lcom/owlen/app/presentation/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "appContext", "Landroid/content/Context;", "serviceRepository", "Lcom/owlen/app/service/ServiceRepository;", "sessionLogger", "Lcom/owlen/app/data/log/SessionLogger;", "settingsRepository", "Lcom/owlen/app/data/settings/SettingsRepository;", "(Landroid/content/Context;Lcom/owlen/app/service/ServiceRepository;Lcom/owlen/app/data/log/SessionLogger;Lcom/owlen/app/data/settings/SettingsRepository;)V", "_interruption", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/owlen/app/presentation/home/InterruptionInfo;", "interruption", "Lkotlinx/coroutines/flow/StateFlow;", "getInterruption", "()Lkotlinx/coroutines/flow/StateFlow;", "isProtectionActive", "", "lastCalibrationDay", "", "lastDetection", "Lkotlin/Pair;", "Lcom/owlen/app/domain/model/DetectedEvent;", "Lcom/owlen/app/domain/model/DisturbanceResult;", "getLastDetection", "serviceState", "Lcom/owlen/app/service/ServiceState;", "getServiceState", "sessionStartTime", "getSessionStartTime", "settings", "Lcom/owlen/app/domain/model/SleepSettings;", "getSettings", "acknowledgeSafetyAlert", "", "dismissInterruption", "needsCalibrationToday", "setProtectionEnabled", "enabled", "isActive", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.service.ServiceRepository serviceRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.data.log.SessionLogger sessionLogger = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.owlen.app.presentation.home.InterruptionInfo> _interruption = null;
    
    /**
     * Non-null when the last session ended with a system kill the user hasn't reviewed.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.owlen.app.presentation.home.InterruptionInfo> interruption = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> lastCalibrationDay = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.owlen.app.service.ServiceState> serviceState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> sessionStartTime = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<kotlin.Pair<com.owlen.app.domain.model.DetectedEvent, com.owlen.app.domain.model.DisturbanceResult>> lastDetection = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.owlen.app.domain.model.SleepSettings> settings = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isProtectionActive = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context appContext, @org.jetbrains.annotations.NotNull()
    com.owlen.app.service.ServiceRepository serviceRepository, @org.jetbrains.annotations.NotNull()
    com.owlen.app.data.log.SessionLogger sessionLogger, @org.jetbrains.annotations.NotNull()
    com.owlen.app.data.settings.SettingsRepository settingsRepository) {
        super();
    }
    
    /**
     * Non-null when the last session ended with a system kill the user hasn't reviewed.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.owlen.app.presentation.home.InterruptionInfo> getInterruption() {
        return null;
    }
    
    public final void dismissInterruption() {
    }
    
    /**
     * True when calibration has not run yet today (spec: recalibrate daily).
     */
    public final boolean needsCalibrationToday() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.owlen.app.service.ServiceState> getServiceState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getSessionStartTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<kotlin.Pair<com.owlen.app.domain.model.DetectedEvent, com.owlen.app.domain.model.DisturbanceResult>> getLastDetection() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.owlen.app.domain.model.SleepSettings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isProtectionActive() {
        return null;
    }
    
    public final void setProtectionEnabled(boolean enabled) {
    }
    
    public final void acknowledgeSafetyAlert() {
    }
    
    private final boolean isActive(com.owlen.app.service.ServiceState $this$isActive) {
        return false;
    }
}