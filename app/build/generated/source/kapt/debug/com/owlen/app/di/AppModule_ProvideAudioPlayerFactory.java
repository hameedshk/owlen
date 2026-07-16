package com.owlen.app.di;

import com.owlen.app.data.audio.AudioPlayer;
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
public final class AppModule_ProvideAudioPlayerFactory implements Factory<AudioPlayer> {
  @Override
  public AudioPlayer get() {
    return provideAudioPlayer();
  }

  public static AppModule_ProvideAudioPlayerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AudioPlayer provideAudioPlayer() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAudioPlayer());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideAudioPlayerFactory INSTANCE = new AppModule_ProvideAudioPlayerFactory();
  }
}
