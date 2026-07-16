package com.owlen.app.service;

import com.owlen.app.data.audio.AudioCapture;
import com.owlen.app.data.audio.AudioPlayer;
import com.owlen.app.data.log.SessionLogger;
import com.owlen.app.data.ml.EventDetector;
import com.owlen.app.data.ml.FeatureExtractor;
import com.owlen.app.data.settings.SettingsRepository;
import com.owlen.app.domain.policy.PolicyEngine;
import com.owlen.app.domain.scorer.DisturbanceScorer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SleepProtectionService_MembersInjector implements MembersInjector<SleepProtectionService> {
  private final Provider<AudioCapture> audioCaptureProvider;

  private final Provider<AudioPlayer> audioPlayerProvider;

  private final Provider<FeatureExtractor> featureExtractorProvider;

  private final Provider<EventDetector> eventDetectorProvider;

  private final Provider<DisturbanceScorer> disturbanceScorerProvider;

  private final Provider<PolicyEngine> policyEngineProvider;

  private final Provider<SessionLogger> sessionLoggerProvider;

  private final Provider<ServiceRepository> serviceRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public SleepProtectionService_MembersInjector(Provider<AudioCapture> audioCaptureProvider,
      Provider<AudioPlayer> audioPlayerProvider,
      Provider<FeatureExtractor> featureExtractorProvider,
      Provider<EventDetector> eventDetectorProvider,
      Provider<DisturbanceScorer> disturbanceScorerProvider,
      Provider<PolicyEngine> policyEngineProvider, Provider<SessionLogger> sessionLoggerProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.audioCaptureProvider = audioCaptureProvider;
    this.audioPlayerProvider = audioPlayerProvider;
    this.featureExtractorProvider = featureExtractorProvider;
    this.eventDetectorProvider = eventDetectorProvider;
    this.disturbanceScorerProvider = disturbanceScorerProvider;
    this.policyEngineProvider = policyEngineProvider;
    this.sessionLoggerProvider = sessionLoggerProvider;
    this.serviceRepositoryProvider = serviceRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  public static MembersInjector<SleepProtectionService> create(
      Provider<AudioCapture> audioCaptureProvider, Provider<AudioPlayer> audioPlayerProvider,
      Provider<FeatureExtractor> featureExtractorProvider,
      Provider<EventDetector> eventDetectorProvider,
      Provider<DisturbanceScorer> disturbanceScorerProvider,
      Provider<PolicyEngine> policyEngineProvider, Provider<SessionLogger> sessionLoggerProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new SleepProtectionService_MembersInjector(audioCaptureProvider, audioPlayerProvider, featureExtractorProvider, eventDetectorProvider, disturbanceScorerProvider, policyEngineProvider, sessionLoggerProvider, serviceRepositoryProvider, settingsRepositoryProvider);
  }

  @Override
  public void injectMembers(SleepProtectionService instance) {
    injectAudioCapture(instance, audioCaptureProvider.get());
    injectAudioPlayer(instance, audioPlayerProvider.get());
    injectFeatureExtractor(instance, featureExtractorProvider.get());
    injectEventDetector(instance, eventDetectorProvider.get());
    injectDisturbanceScorer(instance, disturbanceScorerProvider.get());
    injectPolicyEngine(instance, policyEngineProvider.get());
    injectSessionLogger(instance, sessionLoggerProvider.get());
    injectServiceRepository(instance, serviceRepositoryProvider.get());
    injectSettingsRepository(instance, settingsRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.audioCapture")
  public static void injectAudioCapture(SleepProtectionService instance,
      AudioCapture audioCapture) {
    instance.audioCapture = audioCapture;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.audioPlayer")
  public static void injectAudioPlayer(SleepProtectionService instance, AudioPlayer audioPlayer) {
    instance.audioPlayer = audioPlayer;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.featureExtractor")
  public static void injectFeatureExtractor(SleepProtectionService instance,
      FeatureExtractor featureExtractor) {
    instance.featureExtractor = featureExtractor;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.eventDetector")
  public static void injectEventDetector(SleepProtectionService instance,
      EventDetector eventDetector) {
    instance.eventDetector = eventDetector;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.disturbanceScorer")
  public static void injectDisturbanceScorer(SleepProtectionService instance,
      DisturbanceScorer disturbanceScorer) {
    instance.disturbanceScorer = disturbanceScorer;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.policyEngine")
  public static void injectPolicyEngine(SleepProtectionService instance,
      PolicyEngine policyEngine) {
    instance.policyEngine = policyEngine;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.sessionLogger")
  public static void injectSessionLogger(SleepProtectionService instance,
      SessionLogger sessionLogger) {
    instance.sessionLogger = sessionLogger;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.serviceRepository")
  public static void injectServiceRepository(SleepProtectionService instance,
      ServiceRepository serviceRepository) {
    instance.serviceRepository = serviceRepository;
  }

  @InjectedFieldSignature("com.owlen.app.service.SleepProtectionService.settingsRepository")
  public static void injectSettingsRepository(SleepProtectionService instance,
      SettingsRepository settingsRepository) {
    instance.settingsRepository = settingsRepository;
  }
}
