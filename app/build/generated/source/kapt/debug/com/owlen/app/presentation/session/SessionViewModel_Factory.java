package com.owlen.app.presentation.session;

import com.owlen.app.data.log.SessionLogger;
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
public final class SessionViewModel_Factory implements Factory<SessionViewModel> {
  private final Provider<SessionLogger> sessionLoggerProvider;

  public SessionViewModel_Factory(Provider<SessionLogger> sessionLoggerProvider) {
    this.sessionLoggerProvider = sessionLoggerProvider;
  }

  @Override
  public SessionViewModel get() {
    return newInstance(sessionLoggerProvider.get());
  }

  public static SessionViewModel_Factory create(Provider<SessionLogger> sessionLoggerProvider) {
    return new SessionViewModel_Factory(sessionLoggerProvider);
  }

  public static SessionViewModel newInstance(SessionLogger sessionLogger) {
    return new SessionViewModel(sessionLogger);
  }
}
