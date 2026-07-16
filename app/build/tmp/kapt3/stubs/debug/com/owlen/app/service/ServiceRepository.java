package com.owlen.app.service;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0007J\u0015\u0010\u0018\u001a\u00020\u00152\b\u0010\u0019\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\u001aJ\u000e\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u000bR\"\u0010\u0003\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R%\u0010\f\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00050\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0019\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000f\u00a8\u0006\u001d"}, d2 = {"Lcom/owlen/app/service/ServiceRepository;", "", "()V", "_lastDetection", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lkotlin/Pair;", "Lcom/owlen/app/domain/model/DetectedEvent;", "Lcom/owlen/app/domain/model/DisturbanceResult;", "_sessionStartTime", "", "_state", "Lcom/owlen/app/service/ServiceState;", "lastDetection", "Lkotlinx/coroutines/flow/StateFlow;", "getLastDetection", "()Lkotlinx/coroutines/flow/StateFlow;", "sessionStartTime", "getSessionStartTime", "state", "getState", "updateLastDetection", "", "event", "result", "updateSessionStartTime", "time", "(Ljava/lang/Long;)V", "updateState", "newState", "app_debug"})
public final class ServiceRepository {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.owlen.app.service.ServiceState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.owlen.app.service.ServiceState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _sessionStartTime = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> sessionStartTime = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<kotlin.Pair<com.owlen.app.domain.model.DetectedEvent, com.owlen.app.domain.model.DisturbanceResult>> _lastDetection = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<kotlin.Pair<com.owlen.app.domain.model.DetectedEvent, com.owlen.app.domain.model.DisturbanceResult>> lastDetection = null;
    
    @javax.inject.Inject()
    public ServiceRepository() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.owlen.app.service.ServiceState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getSessionStartTime() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<kotlin.Pair<com.owlen.app.domain.model.DetectedEvent, com.owlen.app.domain.model.DisturbanceResult>> getLastDetection() {
        return null;
    }
    
    public final void updateState(@org.jetbrains.annotations.NotNull()
    com.owlen.app.service.ServiceState newState) {
    }
    
    public final void updateSessionStartTime(@org.jetbrains.annotations.Nullable()
    java.lang.Long time) {
    }
    
    public final void updateLastDetection(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DetectedEvent event, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DisturbanceResult result) {
    }
}