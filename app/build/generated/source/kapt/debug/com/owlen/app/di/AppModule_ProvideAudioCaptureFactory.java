package com.owlen.app.di;

import com.owlen.app.data.audio.AudioCapture;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideAudioCaptureFactory implements Factory<AudioCapture> {
  @Override
  public AudioCapture get() {
    return provideAudioCapture();
  }

  public static AppModule_ProvideAudioCaptureFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AudioCapture provideAudioCapture() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAudioCapture());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideAudioCaptureFactory INSTANCE = new AppModule_ProvideAudioCaptureFactory();
  }
}
