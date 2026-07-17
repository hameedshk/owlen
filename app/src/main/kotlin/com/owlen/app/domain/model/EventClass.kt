package com.owlen.app.domain.model

enum class EventClass(val displayName: String) {
    AIRCRAFT("Aircraft"),
    ALARM_CLOCK("Alarm Clock"),
    BIRD("Bird"),
    CAR_ALARM("Car Alarm"),
    CAR_HORN("Car Horn"),
    CAT("Cat"),
    CONSTRUCTION("Construction"),
    DOG_BARKING("Dog Barking"),
    DOOR_KNOCK("Door/Knock"),
    FIREWORKS("Fireworks"),
    GARBAGE_COLLECTION("Garbage Collection"),
    GLASS_BREAK("Glass Break"),
    HUMAN_SHOUTING("Human Shouting"),
    MOTORCYCLE("Motorcycle"),
    MUSIC("Music"),
    RAIN("Rain"),
    SIREN("Siren"),
    SNORING("Snoring"),
    SPEECH("Speech"),
    THUNDER("Thunder"),
    TRAFFIC("Traffic"),
    WIND("Wind"),
    BABY_CRY("Baby Cry"),
    SMOKE_ALARM("Smoke Alarm"),
    CUSTOM("Custom Sound"),
    UNKNOWN("Unknown");

    val isSafetyEvent: Boolean
        get() = this == BABY_CRY || this == SMOKE_ALARM
}
