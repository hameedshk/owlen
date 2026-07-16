package com.owlen.app.data.ml

import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.EventClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Maps YAMNet's 521 AudioSet class scores to Owlen's 10 event classes.
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
        const val DETECTION_FLOOR = 0.10f
        const val CONFIDENCE_GAIN = 2.0f

        private val CLASS_MAP: Map<EventClass, IntArray> = mapOf(
            // Shout, Bellow, Yell, Children shouting, Screaming
            EventClass.HUMAN_SHOUTING to intArrayOf(6, 7, 9, 10, 11),
            // Crying/sobbing, Baby cry/infant cry, Whimper
            EventClass.BABY_CRY to intArrayOf(19, 20, 21),
            // Dog, Bark, Yip, Howl, Bow-wow, Growling, Whimper (dog)
            EventClass.DOG_BARKING to intArrayOf(69, 70, 71, 72, 73, 74, 75),
            // Thunderstorm, Thunder
            EventClass.THUNDER to intArrayOf(280, 281),
            // Rain, Raindrop, Rain on surface
            EventClass.RAIN to intArrayOf(283, 284, 285),
            // Truck, Air brake, Reversing beeps
            EventClass.GARBAGE_COLLECTION to intArrayOf(310, 311, 313),
            // Motorcycle, Accelerating/revving/vroom
            EventClass.MOTORCYCLE to intArrayOf(320, 347),
            // Smoke detector/smoke alarm, Fire alarm
            EventClass.SMOKE_ALARM to intArrayOf(393, 394),
            // Chainsaw, Tools, Hammer, Jackhammer, Sawing, Power tool, Drill
            EventClass.CONSTRUCTION to intArrayOf(341, 412, 413, 414, 415, 418, 419)
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
