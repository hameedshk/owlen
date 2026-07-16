package com.owlen.app.presentation.ui.theme;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aD\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u001c\u0010\b\u001a\u0018\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t\u00a2\u0006\u0002\b\u000b\u00a2\u0006\u0002\b\fH\u0007\u001a\u0010\u0010\r\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0003\u001a/\u0010\u000e\u001a\u00020\u00012\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0011\u0010\b\u001a\r\u0012\u0004\u0012\u00020\u00010\u0011\u00a2\u0006\u0002\b\u000bH\u0007\u001a\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\u0015\u001a\u00020\u0010H\u0002\u001a$\u0010\u0016\u001a\u00020\u0001*\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u0005H\u0002\u001a0\u0010\u001b\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020\u0005\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u001a.\u0010#\u001a\u00020\u0003*\u00020\u00032\u0006\u0010$\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020&2\b\b\u0002\u0010\'\u001a\u00020\u0005\u00f8\u0001\u0000\u00a2\u0006\u0004\b(\u0010)\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006*"}, d2 = {"AmbientBackground", "", "modifier", "Landroidx/compose/ui/Modifier;", "orbIntensity", "", "animated", "", "content", "Lkotlin/Function1;", "Landroidx/compose/foundation/layout/BoxScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "AnimatedOrbLayer", "FadeSlideIn", "delayMillis", "", "Lkotlin/Function0;", "buildStars", "", "Lcom/owlen/app/presentation/ui/theme/Star;", "count", "drawOrbs", "Landroidx/compose/ui/graphics/drawscope/DrawScope;", "intensity", "drift", "breath", "glass", "shape", "Landroidx/compose/ui/graphics/Shape;", "fill", "Landroidx/compose/ui/graphics/Color;", "borderAlpha", "glass-9LQNqLg", "(Landroidx/compose/ui/Modifier;Landroidx/compose/ui/graphics/Shape;JF)Landroidx/compose/ui/Modifier;", "glow", "color", "radius", "Landroidx/compose/ui/unit/Dp;", "alpha", "glow-jG9AyaU", "(Landroidx/compose/ui/Modifier;JFF)Landroidx/compose/ui/Modifier;", "app_debug"})
public final class GlassKt {
    
    /**
     * One-shot fade + upward slide used to stagger content onto a screen.
     * Give later elements a larger [delayMillis] for a cascading entrance.
     */
    @androidx.compose.runtime.Composable()
    public static final void FadeSlideIn(int delayMillis, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    private static final java.util.List<com.owlen.app.presentation.ui.theme.Star> buildStars(int count) {
        return null;
    }
    
    private static final void drawOrbs(androidx.compose.ui.graphics.drawscope.DrawScope $this$drawOrbs, float intensity, float drift, float breath) {
    }
    
    /**
     * App-wide backdrop: near-black base with dim amber and indigo glow orbs
     * bleeding in from the edges, plus a faint twinkling starfield. When
     * [animated], the orbs drift and breathe slowly; pass `animated = false`
     * on screens that must stay visually still (e.g. active night mode).
     */
    @androidx.compose.runtime.Composable()
    public static final void AmbientBackground(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, float orbIntensity, boolean animated, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super androidx.compose.foundation.layout.BoxScope, kotlin.Unit> content) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void AnimatedOrbLayer(float orbIntensity) {
    }
}