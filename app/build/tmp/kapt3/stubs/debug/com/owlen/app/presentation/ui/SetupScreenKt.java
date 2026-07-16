package com.owlen.app.presentation.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000J\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\u001a&\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a6\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0005H\u0003\u001a\u00cf\u0001\u0010\u0010\u001a\u00020\u00012\b\b\u0002\u0010\u0011\u001a\u00020\n2\b\b\u0002\u0010\u0012\u001a\u00020\n2\b\b\u0002\u0010\u0013\u001a\u00020\n2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072w\b\u0002\u0010\u001a\u001aq\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b(\u001e\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b(\u001f\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b( \u0012\u0013\u0012\u00110\u0015\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b(!\u0012\u0013\u0012\u00110\u0017\u00a2\u0006\f\b\u001c\u0012\b\b\u001d\u0012\u0004\b\b(\"\u0012\u0004\u0012\u00020\u00010\u001bH\u0007\u001a&\u0010#\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\\\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\n2\u0006\u0010\'\u001a\u00020\u00052\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u000eH\u0003\u00a8\u0006+"}, d2 = {"AmPmButton", "", "label", "", "selected", "", "onClick", "Lkotlin/Function0;", "NumberDrumPicker", "value", "", "range", "Lkotlin/ranges/IntRange;", "onValueChange", "Lkotlin/Function1;", "formatWithLeadingZero", "SetupScreen", "initialSleepHour", "initialWakeHour", "initialWakeMinute", "initialSound", "Lcom/owlen/app/domain/model/MaskingSound;", "initialSensitivity", "Lcom/owlen/app/domain/model/Sensitivity;", "onNavigateBack", "onNavigateToCalibration", "onSaveSettings", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "sleepHour", "wakeHour", "wakeMinute", "sound", "sensitivity", "SoundOptionRow", "TimePickerCard", "hour", "minute", "isAm", "onHourChange", "onMinuteChange", "onAmPmChange", "app_debug"})
public final class SetupScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void SetupScreen(int initialSleepHour, int initialWakeHour, int initialWakeMinute, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.MaskingSound initialSound, @org.jetbrains.annotations.NotNull()
    com.owlen.app.domain.model.Sensitivity initialSensitivity, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onNavigateToCalibration, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function5<? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super com.owlen.app.domain.model.MaskingSound, ? super com.owlen.app.domain.model.Sensitivity, kotlin.Unit> onSaveSettings) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TimePickerCard(int hour, int minute, boolean isAm, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onHourChange, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onMinuteChange, kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onAmPmChange) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void NumberDrumPicker(int value, kotlin.ranges.IntRange range, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onValueChange, boolean formatWithLeadingZero) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AmPmButton(java.lang.String label, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SoundOptionRow(java.lang.String label, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}