package com.owlen.app.di;

import com.owlen.app.service.ServiceRepository;
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
public final class AppModule_ProvideServiceRepositoryFactory implements Factory<ServiceRepository> {
  @Override
  public ServiceRepository get() {
    return provideServiceRepository();
  }

  public static AppModule_ProvideServiceRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ServiceRepository provideServiceRepository() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideServiceRepository());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideServiceRepositoryFactory INSTANCE = new AppModule_ProvideServiceRepositoryFactory();
  }
}
