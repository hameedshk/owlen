package com.owlen.app.domain.policy;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J6\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\nJ\u0018\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a8\u0006\u0011"}, d2 = {"Lcom/owlen/app/domain/policy/PolicyEngine;", "", "()V", "evaluate", "Lcom/owlen/app/domain/model/PolicyAction;", "result", "Lcom/owlen/app/domain/model/DisturbanceResult;", "event", "Lcom/owlen/app/domain/model/DetectedEvent;", "currentTimeMs", "", "settings", "Lcom/owlen/app/domain/model/SleepSettings;", "isMaskingActive", "", "lowScoreDurationMs", "isNearWakeTime", "app_debug"})
public final class PolicyEngine {
    
    public PolicyEngine() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.PolicyAction evaluate(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DisturbanceResult result, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DetectedEvent event, long currentTimeMs, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.SleepSettings settings, boolean isMaskingActive, long lowScoreDurationMs) {
        return null;
    }
    
    private final boolean isNearWakeTime(long currentTimeMs, com.owlen.app.domain.model.SleepSettings settings) {
        return false;
    }
}