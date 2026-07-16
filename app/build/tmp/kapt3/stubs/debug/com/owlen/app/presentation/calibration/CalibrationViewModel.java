package com.owlen.app.presentation.calibration;

/**
 * Measures the room's ambient sound level during calibration and persists it
 * as the scorer's soundLevelFloor, so disturbance scores are relative to the
 * user's actual environment.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\b\u0007\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0018\u001a\u00020\u0019J\b\u0010\u001a\u001a\u00020\u0019H\u0014J\u0017\u0010\u001b\u001a\u00020\u00192\b\u0010\u001c\u001a\u0004\u0018\u00010\tH\u0002\u00a2\u0006\u0002\u0010\u001dJ\u0006\u0010\u001e\u001a\u00020\u0019J\u0006\u0010\u001f\u001a\u00020\u0019J\u0006\u0010 \u001a\u00020\u0019J\b\u0010!\u001a\u00020\u0019H\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/owlen/app/presentation/calibration/CalibrationViewModel;", "Landroidx/lifecycle/ViewModel;", "audioCapture", "Lcom/owlen/app/data/audio/AudioCapture;", "settingsRepository", "Lcom/owlen/app/data/settings/SettingsRepository;", "(Lcom/owlen/app/data/audio/AudioCapture;Lcom/owlen/app/data/settings/SettingsRepository;)V", "_currentDbLevel", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_obstructed", "", "consecutiveLowWindows", "", "currentDbLevel", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentDbLevel", "()Lkotlinx/coroutines/flow/StateFlow;", "measureJob", "Lkotlinx/coroutines/Job;", "obstructed", "getObstructed", "samples", "", "finishMeasuring", "", "onCleared", "persistResult", "floor", "(Ljava/lang/Float;)V", "retry", "skip", "startMeasuring", "stopCapture", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CalibrationViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.data.audio.AudioCapture audioCapture = null;
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.data.settings.SettingsRepository settingsRepository = null;
    public static final float OBSTRUCTION_DB_THRESHOLD = 20.0F;
    public static final int OBSTRUCTION_WINDOW_COUNT = 10;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Float> _currentDbLevel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Float> currentDbLevel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _obstructed = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> obstructed = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.Float> samples = null;
    private int consecutiveLowWindows = 0;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job measureJob;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.presentation.calibration.CalibrationViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public CalibrationViewModel(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.audio.AudioCapture audioCapture, @org.jetbrains.annotations.NotNull()
    com.owlen.app.data.settings.SettingsRepository settingsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Float> getCurrentDbLevel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getObstructed() {
        return null;
    }
    
    public final void startMeasuring() {
    }
    
    /**
     * Restart measurement after the user repositions the device.
     */
    public final void retry() {
    }
    
    public final void finishMeasuring() {
    }
    
    /**
     * Skip: keep the configured static floor but still mark today calibrated.
     */
    public final void skip() {
    }
    
    private final void persistResult(java.lang.Float floor) {
    }
    
    private final void stopCapture() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/owlen/app/presentation/calibration/CalibrationViewModel$Companion;", "", "()V", "OBSTRUCTION_DB_THRESHOLD", "", "OBSTRUCTION_WINDOW_COUNT", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}