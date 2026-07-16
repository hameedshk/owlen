package com.owlen.app.presentation.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u001b\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\tH\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010\"\u001a\u00020\rH\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003JQ\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010%\u001a\u00020\t2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020\u0007H\u00d6\u0001J\t\u0010(\u001a\u00020)H\u00d6\u0001R\u0011\u0010\u000e\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006*"}, d2 = {"Lcom/owlen/app/presentation/ui/SessionEventItem;", "", "timestampMs", "", "eventClass", "Lcom/owlen/app/domain/model/EventClass;", "score", "", "wasMasked", "", "maskingSound", "Lcom/owlen/app/domain/model/MaskingSound;", "maskingVolume", "", "durationMs", "(JLcom/owlen/app/domain/model/EventClass;IZLcom/owlen/app/domain/model/MaskingSound;FJ)V", "getDurationMs", "()J", "getEventClass", "()Lcom/owlen/app/domain/model/EventClass;", "getMaskingSound", "()Lcom/owlen/app/domain/model/MaskingSound;", "getMaskingVolume", "()F", "getScore", "()I", "getTimestampMs", "getWasMasked", "()Z", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"})
public final class SessionEventItem {
    private final long timestampMs = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.domain.model.EventClass eventClass = null;
    private final int score = 0;
    private final boolean wasMasked = false;
    @org.jetbrains.annotations.Nullable()
    private final com.owlen.app.domain.model.MaskingSound maskingSound = null;
    private final float maskingVolume = 0.0F;
    private final long durationMs = 0L;
    
    public SessionEventItem(long timestampMs, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.EventClass eventClass, int score, boolean wasMasked, @org.jetbrains.annotations.Nullable()
    com.owlen.app.domain.model.MaskingSound maskingSound, float maskingVolume, long durationMs) {
        super();
    }
    
    public final long getTimestampMs() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.EventClass getEventClass() {
        return null;
    }
    
    public final int getScore() {
        return 0;
    }
    
    public final boolean getWasMasked() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.owlen.app.domain.model.MaskingSound getMaskingSound() {
        return null;
    }
    
    public final float getMaskingVolume() {
        return 0.0F;
    }
    
    public final long getDurationMs() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.EventClass component2() {
        return null;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.owlen.app.domain.model.MaskingSound component5() {
        return null;
    }
    
    public final float component6() {
        return 0.0F;
    }
    
    public final long component7() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.presentation.ui.SessionEventItem copy(long timestampMs, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.EventClass eventClass, int score, boolean wasMasked, @org.jetbrains.annotations.Nullable()
    com.owlen.app.domain.model.MaskingSound maskingSound, float maskingVolume, long durationMs) {
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