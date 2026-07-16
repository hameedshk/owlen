package com.owlen.app.di;

import android.content.Context;
import com.owlen.app.data.log.SessionLogger;
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
public final class AppModule_ProvideSessionLoggerFactory implements Factory<SessionLogger> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideSessionLoggerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SessionLogger get() {
    return provideSessionLogger(contextProvider.get());
  }

  public static AppModule_ProvideSessionLoggerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideSessionLoggerFactory(contextProvider);
  }

  public static SessionLogger provideSessionLogger(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSessionLogger(context));
  }
}
