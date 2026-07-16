package com.owlen.app.service

import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound

sealed class ServiceState {
    object Idle : ServiceState()
    object Monitoring : ServiceState()
    data class MaskingActive(
        val sound: MaskingSound,
        val volume: Float
    ) : ServiceState()
    data class SafetyAlertActive(
        val event: EventClass
    ) : ServiceState()
    data class Interrupted(
        val interruptedAt: Long,
        val resumedAt: Long
    ) : ServiceState()
}
