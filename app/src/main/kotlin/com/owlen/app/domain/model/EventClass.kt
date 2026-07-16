package com.owlen.app.domain.model

enum class EventClass(val displayName: String) {
    GARBAGE_COLLECTION("Garbage Collection"),
    HUMAN_SHOUTING("Human Shouting"),
    MOTORCYCLE("Motorcycle"),
    DOG_BARKING("Dog Barking"),
    CONSTRUCTION("Construction"),
    RAIN("Rain"),
    THUNDER("Thunder"),
    BABY_CRY("Baby Cry"),
    SMOKE_ALARM("Smoke Alarm"),
    UNKNOWN("Unknown");

    val isSafetyEvent: Boolean
        get() = this == BABY_CRY || this == SMOKE_ALARM
}
