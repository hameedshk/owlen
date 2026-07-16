package com.owlen.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0010\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\tH\u00c6\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00072\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u000fR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001d"}, d2 = {"Lcom/owlen/app/domain/model/DetectedEvent;", "", "eventClass", "Lcom/owlen/app/domain/model/EventClass;", "confidence", "", "isSafetyEvent", "", "timestampMs", "", "(Lcom/owlen/app/domain/model/EventClass;FZJ)V", "getConfidence", "()F", "getEventClass", "()Lcom/owlen/app/domain/model/EventClass;", "()Z", "getTimestampMs", "()J", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class DetectedEvent {
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.domain.model.EventClass eventClass = null;
    private final float confidence = 0.0F;
    private final boolean isSafetyEvent = false;
    private final long timestampMs = 0L;
    
    public DetectedEvent(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.EventClass eventClass, float confidence, boolean isSafetyEvent, long timestampMs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.EventClass getEventClass() {
        return null;
    }
    
    public final float getConfidence() {
        return 0.0F;
    }
    
    public final boolean isSafetyEvent() {
        return false;
    }
    
    public final long getTimestampMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.EventClass component1() {
        return null;
    }
    
    public final float component2() {
        return 0.0F;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.DetectedEvent copy(@org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.EventClass eventClass, float confidence, boolean isSafetyEvent, long timestampMs) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}