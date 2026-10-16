package com.owlen.app.presentation.ui.components

import androidx.compose.ui.graphics.Color
import com.owlen.app.domain.model.MaskingSound

/**
 * Color themes and constants for sound scene illustrations.
 * Consolidates all hardcoded colors for easier maintenance and customization.
 */
sealed class SoundSceneTheme {
    abstract val skyStartColor: Color
    abstract val skyEndColor: Color

    object Rain : SoundSceneTheme() {
        override val skyStartColor = Color(0xFF1a237e)  // Deep indigo
        override val skyEndColor = Color(0xFF37474f)    // Slate
        val mountainColor1 = Color(0xFF0d1b4f)
        val mountainColor2 = Color(0xFF1a237e)
        val streakColor = Color.White.copy(alpha = 0.3f)
        val streakAngleRange = 60f..75f
        val streakCount = 20
    }

    object Ocean : SoundSceneTheme() {
        override val skyStartColor = Color(0xFF00897b)  // Teal
        override val skyEndColor = Color(0xFF0d47a1)    // Navy
        val waveColors = listOf(
            Color(0xFF00695c),
            Color(0xFF004d73),
            Color(0xFF0d3b66),
            Color(0xFF001a4d)
        )
        val bandCount = 4
        // Degrees of sine phase advanced per horizontal pixel. The formula is
        // sin((x * frequency + ...) * PI/180), so this must be large enough to
        // sweep multiple full 360° cycles across the card width — 0.01f only
        // swept ~11° total (a flat line). ~1.4f gives a handful of visible
        // crests across a ~1080px-wide card.
        val frequency = 1.4f
        // Phase shift creates depth: each band offset by (band * this) degrees
        // This creates the stacked wave illusion
        val phaseShiftPerBand = 35f
    }

    object Fan : SoundSceneTheme() {
        override val skyStartColor = Color(0xFF78909c)  // Warm grey
        override val skyEndColor = Color(0xFF37474f)    // Charcoal
        val bladeColors = listOf(
            Color(0xFFeceff1),
            Color(0xFFb0bec5),
            Color(0xFF90a4ae),
            Color(0xFF78909c),
            Color(0xFF546e7a)
        )
        val arcCount = 5
        val baseRadius = 30f
        val radiusStep = 15f
        // Starting opacity and fade per arc
        val baseOpacity = 0.5f
        val opacityFadePerArc = 0.08f
    }

    object WhiteNoise : SoundSceneTheme() {
        override val skyStartColor = Color(0xFFcfd8dc)  // Cool grey
        override val skyEndColor = Color(0xFF263238)    // Near-black
        // Radius/alpha bumped up from 0.5f/0.2f — those were sub-pixel at
        // normal screen density and the "static" texture was invisible.
        val dotColor = Color.White.copy(alpha = 0.35f)
        val dotDensity = 16
        val dotRadius = 1.6f
        // Skip probability: 0.2f = 20% skipped (80% drawn)
        // Lower = denser pattern
        val skipProbability = 0.2f
    }

    object PinkNoise : SoundSceneTheme() {
        override val skyStartColor = Color(0xFFf1a1b8)  // Dusty rose
        override val skyEndColor = Color(0xFF7b1fa2)    // Plum
        val bandColors = listOf(
            Color(0xFFf1a1b8),
            Color(0xFFe91e63),
            Color(0xFFC2185b),
            Color(0xFF7b1fa2)
        )
        val bandOpacity = 0.4f
        val bandCount = 4
    }

    object BrownNoise : SoundSceneTheme() {
        override val skyStartColor = Color(0xFFbf360c)  // Amber-brown
        override val skyEndColor = Color(0xFF1a0f00)    // Espresso
        val hillColors = listOf(
            Color(0xFF8b4513),
            Color(0xFF5d4037),
            Color(0xFF3e2723)
        )
        val hillCount = 3
        val baseAmplitude = 20f
        val amplitudeStep = 10f
    }

    companion object {
        fun forSound(sound: MaskingSound): SoundSceneTheme = when (sound) {
            MaskingSound.RAIN -> Rain
            MaskingSound.OCEAN_WAVES -> Ocean
            MaskingSound.FAN -> Fan
            MaskingSound.WHITE_NOISE -> WhiteNoise
            MaskingSound.PINK_NOISE -> PinkNoise
            MaskingSound.BROWN_NOISE -> BrownNoise
        }
    }
}
