package com.owlen.app.di

import android.content.Context
import com.owlen.app.data.audio.AudioCapture
import com.owlen.app.data.audio.AudioPlayer
import com.owlen.app.data.log.SessionLogger
import com.owlen.app.data.ml.EventDetector
import com.owlen.app.data.ml.FeatureExtractor
import com.owlen.app.data.settings.SettingsRepository
import com.owlen.app.domain.policy.PolicyEngine
import com.owlen.app.domain.scorer.DisturbanceScorer
import com.owlen.app.service.ServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAudioCapture(): AudioCapture = AudioCapture()

    @Singleton
    @Provides
    fun provideAudioPlayer(): AudioPlayer = AudioPlayer()

    @Singleton
    @Provides
    fun provideFeatureExtractor(@ApplicationContext context: Context): FeatureExtractor = FeatureExtractor(context)

    @Singleton
    @Provides
    fun provideEventDetector(): EventDetector = EventDetector()

    @Singleton
    @Provides
    fun provideDisturbanceScorer(): DisturbanceScorer = DisturbanceScorer()

    @Singleton
    @Provides
    fun providePolicyEngine(): PolicyEngine = PolicyEngine()

    @Singleton
    @Provides
    fun provideSessionLogger(@ApplicationContext context: Context): SessionLogger = SessionLogger(context)

    @Singleton
    @Provides
    fun provideServiceRepository(): ServiceRepository = ServiceRepository()

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository = SettingsRepository(context)
}
