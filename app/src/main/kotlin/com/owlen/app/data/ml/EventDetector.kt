package com.owlen.app.data.ml

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.EventClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Maps YAMNet's 521 AudioSet class scores to Owlen's 24 event classes.
 *
 * Index constants below are 0-based positions in the yamnet_label_list.txt
 * embedded in the bundled yamnet.tflite. Each Owlen class takes the maximum
 * score across its mapped AudioSet classes.
 *
 * YAMNet scores are independent sigmoids that rarely approach 1.0 even for a
 * clearly present sound, so raw scores are rescaled by CONFIDENCE_GAIN before
 * being reported as confidence (a raw 0.5 maps to full confidence). The
 * DisturbanceScorer's 0.70 safety-bypass threshold therefore corresponds to a
 * raw YAMNet score of 0.35 for Baby Cry / Smoke Alarm.
 */
class EventDetector : AutoCloseable {
    companion object {
        const val NUM_CLASSES = 521
        const val DETECTION_FLOOR = 0.20f
        const val CONFIDENCE_GAIN = 2.0f

        private val CLASS_MAP: Map<EventClass, IntArray> = mapOf(
            // Speech, Male speech, Female speech, Child speech, Conversation, Narration
            EventClass.SPEECH to intArrayOf(0, 1, 2, 3, 4, 5),
            // Shout, Bellow, Yell, Children shouting, Screaming
            EventClass.HUMAN_SHOUTING to intArrayOf(6, 7, 9, 10, 11),
            // Crying/sobbing, Baby cry/infant cry, Whimper
            EventClass.BABY_CRY to intArrayOf(19, 20, 21),
            // Snore, Snoring
            EventClass.SNORING to intArrayOf(24, 25),
            // Dog, Bark, Yip, Howl, Bow-wow, Growling, Whimper (dog)
            EventClass.DOG_BARKING to intArrayOf(69, 70, 71, 72, 73, 74, 75),
            // Cat, Meow, Hiss, Caterwaul
            EventClass.CAT to intArrayOf(76, 78, 79, 80),
            // Bird, Bird vocalization, Chirp/tweet, Rooster
            EventClass.BIRD to intArrayOf(95, 96, 97, 111),
            // Music, Musical instrument, Guitar, Singing
            EventClass.MUSIC to intArrayOf(132, 133, 134, 137),
            // Wind, Howling wind
            EventClass.WIND to intArrayOf(278, 279),
            // Thunderstorm, Thunder
            EventClass.THUNDER to intArrayOf(280, 281),
            // Rain, Raindrop, Rain on surface
            EventClass.RAIN to intArrayOf(283, 284, 285),
            // Fixed-wing aircraft, Helicopter, Propeller, Jet engine
            EventClass.AIRCRAFT to intArrayOf(303, 304, 305, 306),
            // Car, Car passing by, Bus
            EventClass.TRAFFIC to intArrayOf(307, 308, 312),
            // Truck, Air brake, Reversing beeps
            EventClass.GARBAGE_COLLECTION to intArrayOf(310, 311, 313),
            // Emergency vehicle, Ambulance siren, Police car siren, Fire engine siren
            EventClass.SIREN to intArrayOf(314, 315, 316, 317),
            // Car horn/honking, Air horn/truck horn
            EventClass.CAR_HORN to intArrayOf(318, 319),
            // Motorcycle, Accelerating/revving/vroom
            EventClass.MOTORCYCLE to intArrayOf(320, 347),
            // Door, Doorbell, Knock
            EventClass.DOOR_KNOCK to intArrayOf(356, 357, 359),
            // Shatter, Glass
            EventClass.GLASS_BREAK to intArrayOf(361, 427),
            // Smoke detector/smoke alarm, Fire alarm
            EventClass.SMOKE_ALARM to intArrayOf(393, 394),
            // Alarm (generic), Car alarm
            EventClass.CAR_ALARM to intArrayOf(395, 396),
            // Alarm clock, Buzzer
            EventClass.ALARM_CLOCK to intArrayOf(397, 400),
            // Chainsaw, Tools, Hammer, Jackhammer, Sawing, Power tool, Drill
            EventClass.CONSTRUCTION to intArrayOf(341, 412, 413, 414, 415, 418, 419),
            // Fireworks, Explosion, Burst/pop, Gunshot
            EventClass.FIREWORKS to intArrayOf(420, 428, 429, 430)
        )
    }

    suspend fun detect(scores: FloatArray): DetectedEvent {
        if (scores.size != NUM_CLASSES) {
            throw IllegalArgumentException("Scores must be $NUM_CLASSES values, got ${scores.size}")
        }

        return withContext(Dispatchers.Default) {
            var bestClass = EventClass.UNKNOWN
            var bestScore = 0f

            for ((eventClass, indices) in CLASS_MAP) {
                var classScore = 0f
                for (index in indices) {
                    if (scores[index] > classScore) classScore = scores[index]
                }
                if (classScore > bestScore) {
                    bestScore = classScore
                    bestClass = eventClass
                }
            }

            val eventClass = if (bestScore < DETECTION_FLOOR) EventClass.UNKNOWN else bestClass
            val confidence = (bestScore * CONFIDENCE_GAIN).coerceIn(0f, 1f)

            DetectedEvent(
                eventClass = eventClass,
                confidence = confidence,
                isSafetyEvent = eventClass.isSafetyEvent,
                timestampMs = System.currentTimeMillis()
            )
        }
    }

    override fun close() {
        // No native resources — kept for pipeline lifecycle symmetry
    }
}
