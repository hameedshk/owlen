package com.owlen.app.presentation.calibration;

import com.owlen.app.data.audio.AudioCapture;
import com.owlen.app.data.settings.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CalibrationViewModel_Factory implements Factory<CalibrationViewModel> {
  private final Provider<AudioCapture> audioCaptureProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public CalibrationViewModel_Factory(Provider<AudioCapture> audioCaptureProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.audioCaptureProvider = audioCaptureProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public CalibrationViewModel get() {
    return newInstance(audioCaptureProvider.get(), settingsRepositoryProvider.get());
  }

  public static CalibrationViewModel_Factory create(Provider<AudioCapture> audioCaptureProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new CalibrationViewModel_Factory(audioCaptureProvider, settingsRepositoryProvider);
  }

  public static CalibrationViewModel newInstance(AudioCapture audioCapture,
      SettingsRepository settingsRepository) {
    return new CalibrationViewModel(audioCapture, settingsRepository);
  }
}
