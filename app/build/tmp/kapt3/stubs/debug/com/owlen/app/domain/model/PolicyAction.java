package com.owlen.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u0003\u0004\u0005\u0006\u0007B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0005\b\t\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/owlen/app/domain/model/PolicyAction;", "", "()V", "Ignore", "SafetyAlert", "SetVolume", "StartMasking", "StopMasking", "Lcom/owlen/app/domain/model/PolicyAction$Ignore;", "Lcom/owlen/app/domain/model/PolicyAction$SafetyAlert;", "Lcom/owlen/app/domain/model/PolicyAction$SetVolume;", "Lcom/owlen/app/domain/model/PolicyAction$StartMasking;", "Lcom/owlen/app/domain/model/PolicyAction$StopMasking;", "app_debug"})
public abstract class PolicyAction {
    
    private PolicyAction() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/owlen/app/domain/model/PolicyAction$Ignore;", "Lcom/owlen/app/domain/model/PolicyAction;", "()V", "app_debug"})
    public static final class Ignore extends com.owlen.app.domain.model.PolicyAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.owlen.app.domain.model.PolicyAction.Ignore INSTANCE = null;
        
        private Ignore() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/owlen/app/domain/model/PolicyAction$SafetyAlert;", "Lcom/owlen/app/domain/model/PolicyAction;", "event", "Lcom/owlen/app/domain/model/EventClass;", "(Lcom/owlen/app/domain/model/EventClass;)V", "getEvent", "()Lcom/owlen/app/domain/model/EventClass;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class SafetyAlert extends com.owlen.app.domain.model.PolicyAction {
        @org.jetbrains.annotations.NotNull()
        private final com.owlen.app.domain.model.EventClass event = null;
        
        public SafetyAlert(@org.jetbrains.annotations.NotNull()
        com.owlen.app.domain.model.EventClass event) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.EventClass getEvent() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.EventClass component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.PolicyAction.SafetyAlert copy(@org.jetbrains.annotations.NotNull()
        com.owlen.app.domain.model.EventClass event) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/owlen/app/domain/model/PolicyAction$SetVolume;", "Lcom/owlen/app/domain/model/PolicyAction;", "volume", "", "(F)V", "getVolume", "()F", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class SetVolume extends com.owlen.app.domain.model.PolicyAction {
        private final float volume = 0.0F;
        
        public SetVolume(float volume) {
        }
        
        public final float getVolume() {
            return 0.0F;
        }
        
        public final float component1() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.PolicyAction.SetVolume copy(float volume) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0016"}, d2 = {"Lcom/owlen/app/domain/model/PolicyAction$StartMasking;", "Lcom/owlen/app/domain/model/PolicyAction;", "sound", "Lcom/owlen/app/domain/model/MaskingSound;", "volume", "", "(Lcom/owlen/app/domain/model/MaskingSound;F)V", "getSound", "()Lcom/owlen/app/domain/model/MaskingSound;", "getVolume", "()F", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class StartMasking extends com.owlen.app.domain.model.PolicyAction {
        @org.jetbrains.annotations.NotNull()
        private final com.owlen.app.domain.model.MaskingSound sound = null;
        private final float volume = 0.0F;
        
        public StartMasking(@org.jetbrains.annotations.NotNull()
        com.owlen.app.domain.model.MaskingSound sound, float volume) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.MaskingSound getSound() {
            return null;
        }
        
        public final float getVolume() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.MaskingSound component1() {
            return null;
        }
        
        public final float component2() {
            return 0.0F;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.owlen.app.domain.model.PolicyAction.StartMasking copy(@org.jetbrains.annotations.NotNull()
        com.owlen.app.domain.model.MaskingSound sound, float volume) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/owlen/app/domain/model/PolicyAction$StopMasking;", "Lcom/owlen/app/domain/model/PolicyAction;", "()V", "app_debug"})
    public static final class StopMasking extends com.owlen.app.domain.model.PolicyAction {
        @org.jetbrains.annotations.NotNull()
        public static final com.owlen.app.domain.model.PolicyAction.StopMasking INSTANCE = null;
        
        private StopMasking() {
        }
    }
}