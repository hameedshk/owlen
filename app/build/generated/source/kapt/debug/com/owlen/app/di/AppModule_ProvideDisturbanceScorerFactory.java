package com.owlen.app.di;

import com.owlen.app.domain.scorer.DisturbanceScorer;
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
public final class AppModule_ProvideDisturbanceScorerFactory implements Factory<DisturbanceScorer> {
  @Override
  public DisturbanceScorer get() {
    return provideDisturbanceScorer();
  }

  public static AppModule_ProvideDisturbanceScorerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DisturbanceScorer provideDisturbanceScorer() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDisturbanceScorer());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideDisturbanceScorerFactory INSTANCE = new AppModule_ProvideDisturbanceScorerFactory();
  }
}
