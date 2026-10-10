package com.owlen.app.presentation.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object OwlenRadius {
    val card = 16.dp
    val cardTight = 12.dp
    val control = 10.dp
    val pill = 28.dp
}

object OwlenSpacing {
    val gutter = 20.dp
    val cardPad = 16.dp
    val rowH = 16.dp
    val rowV = 14.dp
    val itemGap = 12.dp
    val sectionGap = 24.dp
    val minTap = 48.dp
}

val OwlenShapes = Shapes(
    extraSmall = RoundedCornerShape(OwlenRadius.control),
    small = RoundedCornerShape(OwlenRadius.cardTight),
    medium = RoundedCornerShape(OwlenRadius.card),
    large = RoundedCornerShape(OwlenRadius.card),
    extraLarge = RoundedCornerShape(OwlenRadius.pill)
)
