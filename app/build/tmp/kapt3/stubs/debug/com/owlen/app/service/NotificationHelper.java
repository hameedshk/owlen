package com.owlen.app.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\t\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\f\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/owlen/app/service/NotificationHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "createInterruptionNotification", "Landroid/app/Notification;", "interruptedAt", "", "createMaskingNotification", "sound", "Lcom/owlen/app/domain/model/MaskingSound;", "createNotificationChannels", "", "createSafetyNotification", "event", "Lcom/owlen/app/domain/model/EventClass;", "createServiceNotification", "formatTime", "", "timeMs", "Companion", "app_debug"})
public final class NotificationHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PROTECTION_CHANNEL_ID = "owlen_protection";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAFETY_CHANNEL_ID = "owlen_safety";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PROTECTION_CHANNEL_NAME = "Sleep Protection";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAFETY_CHANNEL_NAME = "Safety Alerts";
    @org.jetbrains.annotations.NotNull()
    public static final com.owlen.app.service.NotificationHelper.Companion Companion = null;
    
    public NotificationHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final void createNotificationChannels() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification createServiceNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification createMaskingNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound sound) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification createSafetyNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.EventClass event) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.app.Notification createInterruptionNotification(@org.jetbrains.annotations.NotNull()
    android.content.Context context, long interruptedAt) {
        return null;
    }
    
    private final java.lang.String formatTime(long timeMs) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/owlen/app/service/NotificationHelper$Companion;", "", "()V", "PROTECTION_CHANNEL_ID", "", "PROTECTION_CHANNEL_NAME", "SAFETY_CHANNEL_ID", "SAFETY_CHANNEL_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}