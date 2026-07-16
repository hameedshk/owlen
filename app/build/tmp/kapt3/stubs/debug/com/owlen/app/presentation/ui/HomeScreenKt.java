package com.owlen.app.presentation.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u00a1\u0001\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\u0014\b\u0002\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00112\u000e\b\u0002\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00010\u000e2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00a2\u0006\u0002\u0010\u0014\u001a0\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u001a \u0010\u001d\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001f\u001a\u00020\u0019H\u0003\u00a8\u0006 "}, d2 = {"HomeScreen", "", "isProtectionActive", "", "sleepHour", "", "wakeHour", "preferredSound", "Lcom/owlen/app/domain/model/MaskingSound;", "sensitivity", "Lcom/owlen/app/domain/model/Sensitivity;", "interruptedAtMs", "", "onInterruptionClick", "Lkotlin/Function0;", "onInterruptionDismiss", "onProtectionToggle", "Lkotlin/Function1;", "onNavigateToSettings", "onNavigateToActive", "(ZIILcom/owlen/app/domain/model/MaskingSound;Lcom/owlen/app/domain/model/Sensitivity;Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "QuickInfoChip", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "text", "", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "ScheduleTimeItem", "label", "time", "app_debug"})
public final class HomeScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(boolean isProtectionActive, int sleepHour, int wakeHour, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound preferredSound, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.Sensitivity sensitivity, @org.jetbrains.annotations.Nullable()
    java.lang.Long interruptedAtMs, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onInterruptionClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onInterruptionDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onProtectionToggle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToSettings, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToActive) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ScheduleTimeItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, java.lang.String time) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickInfoChip(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String text, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}