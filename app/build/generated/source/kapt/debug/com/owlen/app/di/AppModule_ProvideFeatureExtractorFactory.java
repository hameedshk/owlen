package com.owlen.app.di;

import android.content.Context;
import com.owlen.app.data.ml.FeatureExtractor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideFeatureExtractorFactory implements Factory<FeatureExtractor> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideFeatureExtractorFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FeatureExtractor get() {
    return provideFeatureExtractor(contextProvider.get());
  }

  public static AppModule_ProvideFeatureExtractorFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideFeatureExtractorFactory(contextProvider);
  }

  public static FeatureExtractor provideFeatureExtractor(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFeatureExtractor(context));
  }
}
