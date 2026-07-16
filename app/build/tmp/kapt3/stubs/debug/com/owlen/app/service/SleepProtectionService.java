package com.owlen.app.service;

@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00ca\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 k2\u00020\u0001:\u0001kB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010N\u001a\u00020OH\u0002J\b\u0010P\u001a\u00020OH\u0002J(\u0010Q\u001a\u00020O2\u0006\u0010R\u001a\u00020S2\u0006\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020W2\u0006\u0010X\u001a\u00020\u0010H\u0002J\b\u0010Y\u001a\u00020OH\u0002J\u0014\u0010Z\u001a\u0004\u0018\u00010[2\b\u0010\\\u001a\u0004\u0018\u00010]H\u0016J\b\u0010^\u001a\u00020OH\u0016J\b\u0010_\u001a\u00020OH\u0016J\"\u0010`\u001a\u00020a2\b\u0010\\\u001a\u0004\u0018\u00010]2\u0006\u0010b\u001a\u00020a2\u0006\u0010c\u001a\u00020aH\u0016J\b\u0010d\u001a\u00020OH\u0002J\u0010\u0010e\u001a\u00020O2\u0006\u0010T\u001a\u00020fH\u0002J\b\u0010g\u001a\u00020OH\u0002J\u0010\u0010h\u001a\u00020O2\u0006\u0010i\u001a\u00020jH\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0015\u001a\u00020\u00168\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001e\u0010\u001b\u001a\u00020\u001c8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001e\u0010!\u001a\u00020\"8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u000e\u0010\'\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010.\u001a\u00020/8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001e\u00104\u001a\u0002058\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\u000e\u0010:\u001a\u00020;X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010>\u001a\u00020?8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b@\u0010A\"\u0004\bB\u0010CR\u001e\u0010D\u001a\u00020E8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\bF\u0010G\"\u0004\bH\u0010IR\u000e\u0010J\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010K\u001a\u00060LR\u00020MX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006l"}, d2 = {"Lcom/owlen/app/service/SleepProtectionService;", "Landroid/app/Service;", "()V", "audioCapture", "Lcom/owlen/app/data/audio/AudioCapture;", "getAudioCapture", "()Lcom/owlen/app/data/audio/AudioCapture;", "setAudioCapture", "(Lcom/owlen/app/data/audio/AudioCapture;)V", "audioPlayer", "Lcom/owlen/app/data/audio/AudioPlayer;", "getAudioPlayer", "()Lcom/owlen/app/data/audio/AudioPlayer;", "setAudioPlayer", "(Lcom/owlen/app/data/audio/AudioPlayer;)V", "currentSettings", "Lcom/owlen/app/domain/model/SleepSettings;", "detectorGateActive", "", "detectorGateJob", "Lkotlinx/coroutines/Job;", "disturbanceScorer", "Lcom/owlen/app/domain/scorer/DisturbanceScorer;", "getDisturbanceScorer", "()Lcom/owlen/app/domain/scorer/DisturbanceScorer;", "setDisturbanceScorer", "(Lcom/owlen/app/domain/scorer/DisturbanceScorer;)V", "eventDetector", "Lcom/owlen/app/data/ml/EventDetector;", "getEventDetector", "()Lcom/owlen/app/data/ml/EventDetector;", "setEventDetector", "(Lcom/owlen/app/data/ml/EventDetector;)V", "featureExtractor", "Lcom/owlen/app/data/ml/FeatureExtractor;", "getFeatureExtractor", "()Lcom/owlen/app/data/ml/FeatureExtractor;", "setFeatureExtractor", "(Lcom/owlen/app/data/ml/FeatureExtractor;)V", "isMaskingActive", "lowScoreStartTimeMs", "", "maskingStartTimeMs", "notificationHelper", "Lcom/owlen/app/service/NotificationHelper;", "pipelineJob", "policyEngine", "Lcom/owlen/app/domain/policy/PolicyEngine;", "getPolicyEngine", "()Lcom/owlen/app/domain/policy/PolicyEngine;", "setPolicyEngine", "(Lcom/owlen/app/domain/policy/PolicyEngine;)V", "serviceRepository", "Lcom/owlen/app/service/ServiceRepository;", "getServiceRepository", "()Lcom/owlen/app/service/ServiceRepository;", "setServiceRepository", "(Lcom/owlen/app/service/ServiceRepository;)V", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "serviceStartTimeMs", "serviceStartedByUser", "sessionLogger", "Lcom/owlen/app/data/log/SessionLogger;", "getSessionLogger", "()Lcom/owlen/app/data/log/SessionLogger;", "setSessionLogger", "(Lcom/owlen/app/data/log/SessionLogger;)V", "settingsRepository", "Lcom/owlen/app/data/settings/SettingsRepository;", "getSettingsRepository", "()Lcom/owlen/app/data/settings/SettingsRepository;", "setSettingsRepository", "(Lcom/owlen/app/data/settings/SettingsRepository;)V", "stoppedByUser", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "Landroid/os/PowerManager;", "acquireWakeLock", "", "activateDetectorGate", "handleAction", "action", "Lcom/owlen/app/domain/model/PolicyAction;", "event", "Lcom/owlen/app/domain/model/DetectedEvent;", "result", "Lcom/owlen/app/domain/model/DisturbanceResult;", "settings", "logInterruptionIfUnexpected", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "releaseWakeLock", "showSafetyAlert", "Lcom/owlen/app/domain/model/EventClass;", "startPipeline", "updateNotification", "notification", "Landroid/app/Notification;", "Companion", "app_debug"})
public final class SleepProtectionService extends android.app.Service {
    @javax.inject.Inject()
    public com.owlen.app.data.audio.AudioCapture audioCapture;
    @javax.inject.Inject()
    public com.owlen.app.data.audio.AudioPlayer audioPlayer;
    @javax.inject.Inject()
    public com.owlen.app.data.ml.FeatureExtractor featureExtractor;
    @javax.inject.Inject()
    public com.owlen.app.data.ml.EventDetector eventDetector;
    @javax.inject.Inject()
    public com.owlen.app.domain.scorer.DisturbanceScorer disturbanceScorer;
    @javax.inject.Inject()
    public com.owlen.app.domain.policy.PolicyEngine policyEngine;
    @javax.inject.Inject()
    public com.owlen.app.data.log.SessionLogger sessionLogger;
    @javax.inject.Inject()
    public com.owlen.app.service.ServiceRepository serviceRepository;
    @javax.inject.Inject()
    public com.owlen.app.data.settings.SettingsRepository settingsRepository;
    private com.owlen.app.service.NotificationHelper notificationHelper;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job pipelineJob;
    private android.os.PowerManager.WakeLock wakeLock;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private volatile com.owlen.app.domain.model.SleepSettings currentSettings;
    private boolean isMaskingActive = false;
    private long lowScoreStartTimeMs = 0L;
    private long maskingStartTimeMs = 0L;
    @kotlin.jvm.Volatile()
    private volatile boolean detectorGateActive = false;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job detectorGateJob;
    private boolean serviceStartedByUser = false;
    private long serviceStartTimeMs = 0L;
    private boolean stoppedByUser = false;
    public static final int NOTIFICATION_ID = 1001;
    public static final int SAFETY_NOTIFICATION_ID = 1002;
    public static final int INTERRUPTION_NOTIFICATION_ID = 1003;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String ACTION_STOP = "com.owlen.app.action.STOP_PROTECTION";
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.service.SleepProtectionService.Companion Companion = null;
    
    public SleepProtectionService() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.audio.AudioCapture getAudioCapture() {
        return null;
    }
    
    public final void setAudioCapture(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.audio.AudioCapture p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.audio.AudioPlayer getAudioPlayer() {
        return null;
    }
    
    public final void setAudioPlayer(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.audio.AudioPlayer p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.ml.FeatureExtractor getFeatureExtractor() {
        return null;
    }
    
    public final void setFeatureExtractor(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.ml.FeatureExtractor p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.ml.EventDetector getEventDetector() {
        return null;
    }
    
    public final void setEventDetector(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.ml.EventDetector p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.scorer.DisturbanceScorer getDisturbanceScorer() {
        return null;
    }
    
    public final void setDisturbanceScorer(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.scorer.DisturbanceScorer p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.policy.PolicyEngine getPolicyEngine() {
        return null;
    }
    
    public final void setPolicyEngine(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.policy.PolicyEngine p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.log.SessionLogger getSessionLogger() {
        return null;
    }
    
    public final void setSessionLogger(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.log.SessionLogger p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.service.ServiceRepository getServiceRepository() {
        return null;
    }
    
    public final void setServiceRepository(@org.jetbrains.annotations.NotNull()
    com.owlen.app.service.ServiceRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.settings.SettingsRepository getSettingsRepository() {
        return null;
    }
    
    public final void setSettingsRepository(@org.jetbrains.annotations.NotNull()
    com.owlen.app.data.settings.SettingsRepository p0) {
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    private final void acquireWakeLock() {
    }
    
    private final void releaseWakeLock() {
    }
    
    private final void startPipeline() {
    }
    
    private final void handleAction(com.owlen.app.domain.model.PolicyAction action, com.owlen.app.domain.model.DetectedEvent event, com.owlen.app.domain.model.DisturbanceResult result, com.owlen.app.domain.model.SleepSettings settings) {
    }
    
    private final void activateDetectorGate() {
    }
    
    private final void showSafetyAlert(com.owlen.app.domain.model.EventClass event) {
    }
    
    private final void updateNotification(android.app.Notification notification) {
    }
    
    private final void logInterruptionIfUnexpected() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/owlen/app/service/SleepProtectionService$Companion;", "", "()V", "ACTION_STOP", "", "INTERRUPTION_NOTIFICATION_ID", "", "NOTIFICATION_ID", "SAFETY_NOTIFICATION_ID", "start", "", "context", "Landroid/content/Context;", "stop", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void start(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        public final void stop(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
    }
}