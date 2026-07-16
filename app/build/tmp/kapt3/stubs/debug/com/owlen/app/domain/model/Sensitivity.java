package com.owlen.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2 = {"Lcom/owlen/app/domain/model/Sensitivity;", "", "thresholdOffset", "", "(Ljava/lang/String;II)V", "getThresholdOffset", "()I", "LOW", "MEDIUM", "HIGH", "app_debug"})
public enum Sensitivity {
    /*public static final*/ LOW /* = new LOW(0) */,
    /*public static final*/ MEDIUM /* = new MEDIUM(0) */,
    /*public static final*/ HIGH /* = new HIGH(0) */;
    private final int thresholdOffset = 0;
    
    Sensitivity(int thresholdOffset) {
    }
    
    public final int getThresholdOffset() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.owlen.app.domain.model.Sensitivity> getEntries() {
        return null;
    }
}