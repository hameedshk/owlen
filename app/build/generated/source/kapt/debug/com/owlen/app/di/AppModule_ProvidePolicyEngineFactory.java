package com.owlen.app.di;

import com.owlen.app.domain.policy.PolicyEngine;
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
public final class AppModule_ProvidePolicyEngineFactory implements Factory<PolicyEngine> {
  @Override
  public PolicyEngine get() {
    return providePolicyEngine();
  }

  public static AppModule_ProvidePolicyEngineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PolicyEngine providePolicyEngine() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePolicyEngine());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvidePolicyEngineFactory INSTANCE = new AppModule_ProvidePolicyEngineFactory();
  }
}
