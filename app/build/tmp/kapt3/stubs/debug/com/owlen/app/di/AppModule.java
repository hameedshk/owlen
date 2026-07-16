package com.owlen.app.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\nH\u0007J\u0012\u0010\u000b\u001a\u00020\f2\b\b\u0001\u0010\r\u001a\u00020\u000eH\u0007J\b\u0010\u000f\u001a\u00020\u0010H\u0007J\b\u0010\u0011\u001a\u00020\u0012H\u0007J\u0012\u0010\u0013\u001a\u00020\u00142\b\b\u0001\u0010\r\u001a\u00020\u000eH\u0007J\u0012\u0010\u0015\u001a\u00020\u00162\b\b\u0001\u0010\r\u001a\u00020\u000eH\u0007\u00a8\u0006\u0017"}, d2 = {"Lcom/owlen/app/di/AppModule;", "", "()V", "provideAudioCapture", "Lcom/owlen/app/data/audio/AudioCapture;", "provideAudioPlayer", "Lcom/owlen/app/data/audio/AudioPlayer;", "provideDisturbanceScorer", "Lcom/owlen/app/domain/scorer/DisturbanceScorer;", "provideEventDetector", "Lcom/owlen/app/data/ml/EventDetector;", "provideFeatureExtractor", "Lcom/owlen/app/data/ml/FeatureExtractor;", "context", "Landroid/content/Context;", "providePolicyEngine", "Lcom/owlen/app/domain/policy/PolicyEngine;", "provideServiceRepository", "Lcom/owlen/app/service/ServiceRepository;", "provideSessionLogger", "Lcom/owlen/app/data/log/SessionLogger;", "provideSettingsRepository", "Lcom/owlen/app/data/settings/SettingsRepository;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.audio.AudioCapture provideAudioCapture() {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.audio.AudioPlayer provideAudioPlayer() {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.ml.FeatureExtractor provideFeatureExtractor(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.ml.EventDetector provideEventDetector() {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.scorer.DisturbanceScorer provideDisturbanceScorer() {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.policy.PolicyEngine providePolicyEngine() {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.log.SessionLogger provideSessionLogger(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.service.ServiceRepository provideServiceRepository() {
        return null;
    }
    
    @javax.inject.Singleton()
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.data.settings.SettingsRepository provideSettingsRepository(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}