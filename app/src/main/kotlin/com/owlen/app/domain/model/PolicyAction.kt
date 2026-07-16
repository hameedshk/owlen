package com.owlen.app.domain.model

sealed class PolicyAction {
    object Ignore : PolicyAction()
    data class StartMasking(val sound: MaskingSound, val volume: Float) : PolicyAction()
    data class SetVolume(val volume: Float) : PolicyAction()
    object StopMasking : PolicyAction()
    data class SafetyAlert(val event: EventClass) : PolicyAction()
}
