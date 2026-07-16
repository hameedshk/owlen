package com.owlen.app.presentation.home;

import android.content.Context;
import com.owlen.app.data.log.SessionLogger;
import com.owlen.app.data.settings.SettingsRepository;
import com.owlen.app.service.ServiceRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<Context> appContextProvider;

  private final Provider<ServiceRepository> serviceRepositoryProvider;

  private final Provider<SessionLogger> sessionLoggerProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public HomeViewModel_Factory(Provider<Context> appContextProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<SessionLogger> sessionLoggerProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.appContextProvider = appContextProvider;
    this.serviceRepositoryProvider = serviceRepositoryProvider;
    this.sessionLoggerProvider = sessionLoggerProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(appContextProvider.get(), serviceRepositoryProvider.get(), sessionLoggerProvider.get(), settingsRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<Context> appContextProvider,
      Provider<ServiceRepository> serviceRepositoryProvider,
      Provider<SessionLogger> sessionLoggerProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new HomeViewModel_Factory(appContextProvider, serviceRepositoryProvider, sessionLoggerProvider, settingsRepositoryProvider);
  }

  public static HomeViewModel newInstance(Context appContext, ServiceRepository serviceRepository,
      SessionLogger sessionLogger, SettingsRepository settingsRepository) {
    return new HomeViewModel(appContext, serviceRepository, sessionLogger, settingsRepository);
  }
}
