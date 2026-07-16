package com.owlen.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b!\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 32\u00020\u0001:\u00013BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\n\u00a2\u0006\u0002\u0010\u0012J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\nH\u00c6\u0003J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\bH\u00c6\u0003J\t\u0010)\u001a\u00020\nH\u00c6\u0003J\t\u0010*\u001a\u00020\fH\u00c6\u0003J\t\u0010+\u001a\u00020\u000eH\u00c6\u0003J\t\u0010,\u001a\u00020\u0010H\u00c6\u0003Jm\u0010-\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\nH\u00c6\u0001J\u0013\u0010.\u001a\u00020\u00102\b\u0010/\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00100\u001a\u00020\u0003H\u00d6\u0001J\t\u00101\u001a\u000202H\u00d6\u0001R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001eR\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0016R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001eR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001e\u00a8\u00064"}, d2 = {"Lcom/owlen/app/domain/model/SleepSettings;", "", "sleepWindowStartHour", "", "sleepWindowEndHour", "wakeTimeHour", "wakeTimeMinute", "sensitivity", "Lcom/owlen/app/domain/model/Sensitivity;", "maxVolume", "", "preferredSound", "Lcom/owlen/app/domain/model/MaskingSound;", "autoStopDurationMs", "", "rainBehaviourEnabled", "", "soundLevelFloor", "(IIIILcom/owlen/app/domain/model/Sensitivity;FLcom/owlen/app/domain/model/MaskingSound;JZF)V", "getAutoStopDurationMs", "()J", "getMaxVolume", "()F", "getPreferredSound", "()Lcom/owlen/app/domain/model/MaskingSound;", "getRainBehaviourEnabled", "()Z", "getSensitivity", "()Lcom/owlen/app/domain/model/Sensitivity;", "getSleepWindowEndHour", "()I", "getSleepWindowStartHour", "getSoundLevelFloor", "getWakeTimeHour", "getWakeTimeMinute", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "", "Companion", "app_debug"})
public final class SleepSettings {
    private final int sleepWindowStartHour = 0;
    private final int sleepWindowEndHour = 0;
    private final int wakeTimeHour = 0;
    private final int wakeTimeMinute = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.domain.model.Sensitivity sensitivity = null;
    private final float maxVolume = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final com.owlen.app.domain.model.MaskingSound preferredSound = null;
    private final long autoStopDurationMs = 0L;
    private final boolean rainBehaviourEnabled = false;
    private final float soundLevelFloor = 0.0F;
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.domain.model.SleepSettings.Companion Companion = null;
    
    public SleepSettings(int sleepWindowStartHour, int sleepWindowEndHour, int wakeTimeHour, int wakeTimeMinute, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.Sensitivity sensitivity, float maxVolume, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound preferredSound, long autoStopDurationMs, boolean rainBehaviourEnabled, float soundLevelFloor) {
        super();
    }
    
    public final int getSleepWindowStartHour() {
        return 0;
    }
    
    public final int getSleepWindowEndHour() {
        return 0;
    }
    
    public final int getWakeTimeHour() {
        return 0;
    }
    
    public final int getWakeTimeMinute() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.Sensitivity getSensitivity() {
        return null;
    }
    
    public final float getMaxVolume() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.MaskingSound getPreferredSound() {
        return null;
    }
    
    public final long getAutoStopDurationMs() {
        return 0L;
    }
    
    public final boolean getRainBehaviourEnabled() {
        return false;
    }
    
    public final float getSoundLevelFloor() {
        return 0.0F;
    }
    
    public final int component1() {
        return 0;
    }
    
    public final float component10() {
        return 0.0F;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.Sensitivity component5() {
        return null;
    }
    
    public final float component6() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.MaskingSound component7() {
        return null;
    }
    
    public final long component8() {
        return 0L;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.owlen.app.domain.model.SleepSettings copy(int sleepWindowStartHour, int sleepWindowEndHour, int wakeTimeHour, int wakeTimeMinute, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.Sensitivity sensitivity, float maxVolume, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound preferredSound, long autoStopDurationMs, boolean rainBehaviourEnabled, float soundLevelFloor) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/owlen/app/domain/model/SleepSettings$Companion;", "", "()V", "default", "Lcom/owlen/app/domain/model/SleepSettings;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}