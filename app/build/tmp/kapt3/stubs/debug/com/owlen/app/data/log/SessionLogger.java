package com.owlen.app.data.log;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u001e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\b\b\u0002\u0010\u0019\u001a\u00020\u001aH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0018\u0010\u001c\u001a\u0004\u0018\u00010\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u000e\u0010\u001e\u001a\u00020\u001fH\u0086@\u00a2\u0006\u0002\u0010\u0013J2\u0010 \u001a\u00020\f2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\u0010H\u0086@\u00a2\u0006\u0002\u0010(J&\u0010)\u001a\u00020\f2\u0006\u0010*\u001a\u00020\u00102\u0006\u0010+\u001a\u00020\u00102\u0006\u0010,\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010-J\u0016\u0010.\u001a\u00020\f2\u0006\u0010/\u001a\u000200H\u0086@\u00a2\u0006\u0002\u00101J\u000e\u00102\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0013J\b\u00103\u001a\u00020\fH\u0002J\u000e\u00104\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0010\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020\u0006H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/owlen/app/data/log/SessionLogger;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "currentSession", "Lcom/owlen/app/data/log/SessionLog;", "json", "Lkotlinx/serialization/json/Json;", "sessionsDir", "Ljava/io/File;", "closeSession", "", "sessionId", "", "endTimeMs", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "endSession", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findInterruptedSession", "Lcom/owlen/app/data/log/InterruptedSession;", "getRecentSessions", "", "Lcom/owlen/app/data/log/SessionSummary;", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSessionDetails", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hasUnreviewedInterruption", "", "logEvent", "event", "Lcom/owlen/app/domain/model/DetectedEvent;", "result", "Lcom/owlen/app/domain/model/DisturbanceResult;", "action", "Lcom/owlen/app/domain/model/PolicyAction;", "maskingDurationMs", "(Lcom/owlen/app/domain/model/DetectedEvent;Lcom/owlen/app/domain/model/DisturbanceResult;Lcom/owlen/app/domain/model/PolicyAction;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logInterruption", "startTime", "endTime", "reason", "(JJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logSafetyEvent", "eventClass", "Lcom/owlen/app/domain/model/EventClass;", "(Lcom/owlen/app/domain/model/EventClass;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markInterruptionReviewed", "purgeOldSessions", "startSession", "writeSessionToFile", "session", "app_debug"})
public final class SessionLogger {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.serialization.json.Json json = null;
    @org.jetbrains.annotations.Nullable()
    private com.owlen.app.data.log.SessionLog currentSession;
    @org.jetbrains.annotations.NotNull()
    private final java.io.File sessionsDir = null;
    
    @javax.inject.Inject()
    public SessionLogger(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object startSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object endSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logEvent(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DetectedEvent event, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.DisturbanceResult result, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.PolicyAction action, @org.jetbrains.annotations.Nullable()
    java.lang.Long maskingDurationMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logSafetyEvent(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.EventClass eventClass, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logInterruption(long startTime, long endTime, @org.jetbrains.annotations.NotNull()
    java.lang.String reason, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getRecentSessions(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.owlen.app.data.log.SessionSummary>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSessionDetails(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.owlen.app.data.log.SessionLog> $completion) {
        return null;
    }
    
    /**
     * Finds the most recent session left without an endTime (excluding the
     * session currently in progress) — evidence the service was killed.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object findInterruptedSession(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.owlen.app.data.log.InterruptedSession> $completion) {
        return null;
    }
    
    /**
     * Closes an interrupted session file so it stops being reported.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object closeSession(@org.jetbrains.annotations.NotNull()
    java.lang.String sessionId, long endTimeMs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object hasUnreviewedInterruption(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markInterruptionReviewed(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void writeSessionToFile(com.owlen.app.data.log.SessionLog session) {
    }
    
    private final void purgeOldSessions() {
    }
}