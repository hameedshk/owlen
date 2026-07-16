package com.owlen.app.di;

import com.owlen.app.data.ml.EventDetector;
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
public final class AppModule_ProvideEventDetectorFactory implements Factory<EventDetector> {
  @Override
  public EventDetector get() {
    return provideEventDetector();
  }

  public static AppModule_ProvideEventDetectorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EventDetector provideEventDetector() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideEventDetector());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideEventDetectorFactory INSTANCE = new AppModule_ProvideEventDetectorFactory();
  }
}
