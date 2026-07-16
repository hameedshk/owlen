package com.owlen.app.presentation.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u001aL\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u000fH\u0003\u001aJ\u0010\u0011\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u000fH\u0007\u001a\u0010\u0010\u0012\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\rH\u0003\u001a$\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019\u001a.\u0010\u001a\u001a\u00020\u00072\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00070\u000f2\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001d"}, d2 = {"DIM_AFTER_MS", "", "DRAIN_PCT_PER_HOUR", "", "HOLD_TO_STOP_MS", "", "ActiveModeContent", "", "sessionStartTimeMs", "batteryPercent", "wakeTimeHour", "wakeTimeMinute", "statusText", "", "onInteraction", "Lkotlin/Function0;", "onStopProtection", "ActiveModeScreen", "DimmedNightFace", "EqualizerBars", "color", "Landroidx/compose/ui/graphics/Color;", "modifier", "Landroidx/compose/ui/Modifier;", "EqualizerBars-DxMtmZc", "(JLandroidx/compose/ui/Modifier;)V", "HoldToStopButton", "onHoldStart", "onStop", "app_debug"})
public final class ActiveModeScreenKt {
    private static final long DIM_AFTER_MS = 10000L;
    private static final int HOLD_TO_STOP_MS = 1200;
    private static final float DRAIN_PCT_PER_HOUR = 1.0F;
    
    @androidx.compose.runtime.Composable()
    public static final void ActiveModeScreen(long sessionStartTimeMs, int batteryPercent, int wakeTimeHour, int wakeTimeMinute, @org.jetbrains.annotations.NotNull()
    java.lang.String statusText, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStopProtection) {
    }
    
    /**
     * Minimal low-luminance face for a dark bedroom: dim clock, faint shield.
     * No white or bright pixels — the screen should not light the room.
     */
    @androidx.compose.runtime.Composable()
    private static final void DimmedNightFace(java.lang.String statusText) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ActiveModeContent(long sessionStartTimeMs, int batteryPercent, int wakeTimeHour, int wakeTimeMinute, java.lang.String statusText, kotlin.jvm.functions.Function0<kotlin.Unit> onInteraction, kotlin.jvm.functions.Function0<kotlin.Unit> onStopProtection) {
    }
    
    /**
     * Press-and-hold stop control. Amber, not safety red — red is reserved for
     * Baby Cry / Smoke Alarm. Holding fills the button; releasing early cancels,
     * so a groggy fumble at 3 AM can't kill protection.
     */
    @androidx.compose.runtime.Composable()
    private static final void HoldToStopButton(kotlin.jvm.functions.Function0<kotlin.Unit> onHoldStart, kotlin.jvm.functions.Function0<kotlin.Unit> onStop, androidx.compose.ui.Modifier modifier) {
    }
}