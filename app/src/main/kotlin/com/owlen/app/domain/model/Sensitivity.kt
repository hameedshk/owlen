package com.owlen.app.domain.model

enum class Sensitivity(val thresholdOffset: Int) {
    LOW(15),
    MEDIUM(0),
    HIGH(-15)
}
