package com.owlen.app.service;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ServiceRepository_Factory implements Factory<ServiceRepository> {
  @Override
  public ServiceRepository get() {
    return newInstance();
  }

  public static ServiceRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ServiceRepository newInstance() {
    return new ServiceRepository();
  }

  private static final class InstanceHolder {
    private static final ServiceRepository_Factory INSTANCE = new ServiceRepository_Factory();
  }
}
