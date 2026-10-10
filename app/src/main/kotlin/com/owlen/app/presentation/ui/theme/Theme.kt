package com.owlen.app.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.owlen.app.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val InterFontFamily = FontFamily(
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Bold),
)

val OwlenTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 48.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    titleLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    labelLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        letterSpacing = 2.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 1.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        letterSpacing = 2.5.sp
    )
)

private val OwlenColorScheme = darkColorScheme(
    background = Background,
    surface = SurfaceCard,
    surfaceVariant = SurfaceSunken,
    surfaceContainer = SurfaceCard,
    surfaceContainerHigh = SurfaceElevated,
    surfaceContainerHighest = SurfaceElevated,
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Primary,
    secondaryContainer = Primary.copy(alpha = 0.2f),
    onSecondary = OnPrimary,
    tertiary = Green,
    onTertiary = OnPrimary,
    error = Safety,
    onError = OnPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = Stroke,
    outlineVariant = Stroke,
    inverseSurface = SurfaceCard,
    scrim = Color.Black.copy(alpha = 0.6f),
)

@Composable
fun OwlenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OwlenColorScheme,
        typography = OwlenTypography,
        shapes = OwlenShapes,
        content = content
    )
}
