package com.owlen.app.data.log

import android.content.Context
import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.MaskingSound
import com.owlen.app.domain.model.PolicyAction
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SessionLoggerTest {
    private lateinit var tempDir: File
    private lateinit var mockContext: Context
    private lateinit var logger: SessionLogger

    @Before
    fun setUp() {
        tempDir = File.createTempFile("sessionlogger", "test").apply { delete() }
        tempDir.mkdirs()

        mockContext = mockk<Context>()
        every { mockContext.filesDir } returns tempDir

        logger = SessionLogger(mockContext)
    }

    @After
    fun tearDown() {
        tempDir.deleteRecursively()
    }

    @Test
    fun startSession_createsValidSession() {
        runBlocking {
            logger.startSession()
            logger.endSession()

            val sessions = logger.getRecentSessions(limit = 1)
            assertTrue(sessions.isNotEmpty(), "Should have created a session")
            assertTrue(sessions[0].startTime > 0, "Session should have start time")
        }
    }

    @Test
    fun logEvent_incrementsEventCount() {
        runBlocking {
            logger.startSession()

            val event = DetectedEvent(
                eventClass = EventClass.DOG_BARKING,
                confidence = 0.85f,
                isSafetyEvent = false,
                timestampMs = System.currentTimeMillis()
            )
            val result = DisturbanceResult(
                score = 65,
                eventWeight = 60,
                normSoundLevel = 70,
                timeWeight = 100,
                isSafetyBypass = false
            )
            val action = PolicyAction.StartMasking(MaskingSound.BROWN_NOISE, 0.6f)

            logger.logEvent(event, result, action, maskingDurationMs = 5000L)
            logger.endSession()

            val sessions = logger.getRecentSessions(limit = 1)
            assertEquals(1, sessions[0].eventCount, "Should have logged 1 event")
        }
    }

    @Test
    fun logSafetyEvent_marksSafetyEventOccurred() {
        runBlocking {
            logger.startSession()
            logger.logSafetyEvent(EventClass.BABY_CRY)
            logger.endSession()

            val sessions = logger.getRecentSessions(limit = 1)
            assertTrue(sessions[0].hasSafetyEvents, "Should have safety events")
        }
    }

    @Test
    fun logInterruption_recordsInterruptionCount() {
        runBlocking {
            logger.startSession()

            val startTime = System.currentTimeMillis()
            val endTime = startTime + 30000L
            logger.logInterruption(startTime, endTime, "Test interruption")
            logger.endSession()

            val sessions = logger.getRecentSessions(limit = 1)
            assertEquals(1, sessions[0].interruptionCount, "Should have recorded interruption")
        }
    }

    @Test
    fun hasUnreviewedInterruption_trackStatus() {
        runBlocking {
            logger.startSession()

            val hasInterruptionBefore = logger.hasUnreviewedInterruption()
            logger.logInterruption(System.currentTimeMillis(), System.currentTimeMillis() + 1000, "Test")
            val hasInterruptionAfter = logger.hasUnreviewedInterruption()

            logger.markInterruptionReviewed()
            val hasInterruptionReviewed = logger.hasUnreviewedInterruption()

            assertTrue(!hasInterruptionBefore, "Should not have interruption initially")
            assertTrue(hasInterruptionAfter, "Should have interruption after logging")
            assertTrue(!hasInterruptionReviewed, "Should not have interruption after marking reviewed")

            logger.endSession()
        }
    }
}
