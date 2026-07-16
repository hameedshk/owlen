package com.owlen.app.domain.model

data class DisturbanceResult(
    val score: Int,
    val eventWeight: Int,
    val normSoundLevel: Int,
    val timeWeight: Int,
    val isSafetyBypass: Boolean
)
