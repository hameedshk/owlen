package com.owlen.app.data.log

import android.content.Context
import com.owlen.app.domain.model.DetectedEvent
import com.owlen.app.domain.model.DisturbanceResult
import com.owlen.app.domain.model.EventClass
import com.owlen.app.domain.model.PolicyAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class EventLog(
    val timestampMs: Long,
    val eventClass: String,
    val confidence: Float,
    val disturbanceScore: Int,
    val action: String,
    val maskingSound: String? = null,
    val maskingVolume: Float? = null,
    val durationMs: Long? = null
)

@Serializable
data class InterruptionLog(
    val startTime: Long,
    val endTime: Long,
    val durationMs: Long,
    val reason: String
)

@Serializable
data class SessionLog(
    val sessionId: String,
    val startTime: Long,
    var endTime: Long? = null,
    val events: MutableList<EventLog> = mutableListOf(),
    val interruptions: MutableList<InterruptionLog> = mutableListOf(),
    var totalMaskingDurationMs: Long = 0L,
    val safetyEvents: MutableList<String> = mutableListOf()
)

/** A session whose file has no endTime: the service died without a clean stop. */
data class InterruptedSession(
    val sessionId: String,
    val startTime: Long,
    val lastActivityMs: Long
)

@Serializable
data class SessionSummary(
    val sessionId: String,
    val startTime: Long,
    val endTime: Long?,
    val protectedDurationMs: Long,
    val eventCount: Int,
    val maskedCount: Int,
    val totalMaskingDurationMs: Long,
    val interruptionCount: Int,
    val hasSafetyEvents: Boolean
)

@Singleton
class SessionLogger @Inject constructor(private val context: Context) {
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }

    private var currentSession: SessionLog? = null
    private val sessionsDir: File

    init {
        sessionsDir = File(context.filesDir, "sessions")
        if (!sessionsDir.exists()) {
            sessionsDir.mkdirs()
        }
    }

    suspend fun startSession() {
        withContext(Dispatchers.IO) {
            purgeOldSessions()
            val sessionId = UUID.randomUUID().toString()
            currentSession = SessionLog(
                sessionId = sessionId,
                startTime = System.currentTimeMillis()
            )
        }
    }

    suspend fun endSession() {
        withContext(Dispatchers.IO) {
            currentSession?.let { session ->
                session.endTime = System.currentTimeMillis()
                writeSessionToFile(session)
                currentSession = null
            }
        }
    }

    suspend fun logEvent(
        event: DetectedEvent,
        result: DisturbanceResult,
        action: PolicyAction,
        maskingDurationMs: Long? = null
    ) {
        withContext(Dispatchers.IO) {
            currentSession?.let { session ->
                val actionName = when (action) {
                    is PolicyAction.StartMasking -> "MASKED"
                    is PolicyAction.SetVolume -> "VOLUME_ADJUSTED"
                    PolicyAction.StopMasking -> "MASKING_STOPPED"
                    is PolicyAction.SafetyAlert -> "SAFETY_ALERT"
                    PolicyAction.Ignore -> "IGNORED"
                }

                val maskingSound = (action as? PolicyAction.StartMasking)?.sound?.name
                val maskingVolume = (action as? PolicyAction.StartMasking)?.volume

                val eventLog = EventLog(
                    timestampMs = System.currentTimeMillis(),
                    eventClass = event.eventClass.name,
                    confidence = event.confidence,
                    disturbanceScore = result.score,
                    action = actionName,
                    maskingSound = maskingSound,
                    maskingVolume = maskingVolume,
                    durationMs = maskingDurationMs
                )

                session.events.add(eventLog)

                if (maskingDurationMs != null) {
                    session.totalMaskingDurationMs += maskingDurationMs
                }

                writeSessionToFile(session)
            }
        }
    }

    suspend fun logSafetyEvent(eventClass: EventClass) {
        withContext(Dispatchers.IO) {
            currentSession?.let { session ->
                session.safetyEvents.add(eventClass.name)
                writeSessionToFile(session)
            }
        }
    }

    suspend fun logInterruption(startTime: Long, endTime: Long, reason: String) {
        withContext(Dispatchers.IO) {
            currentSession?.let { session ->
                val duration = endTime - startTime
                val interruption = InterruptionLog(
                    startTime = startTime,
                    endTime = endTime,
                    durationMs = duration,
                    reason = reason
                )
                session.interruptions.add(interruption)
                writeSessionToFile(session)
            }
        }
    }

    suspend fun getRecentSessions(limit: Int = 7): List<SessionSummary> {
        return withContext(Dispatchers.IO) {
            val files = sessionsDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
            files.take(limit).mapNotNull { file ->
                try {
                    val content = file.readText()
                    val log = json.decodeFromString<SessionLog>(content)
                    SessionSummary(
                        sessionId = log.sessionId,
                        startTime = log.startTime,
                        endTime = log.endTime,
                        protectedDurationMs = (log.endTime ?: System.currentTimeMillis()) - log.startTime,
                        eventCount = log.events.size,
                        maskedCount = log.events.count { it.action == "MASKED" },
                        totalMaskingDurationMs = log.totalMaskingDurationMs,
                        interruptionCount = log.interruptions.size,
                        hasSafetyEvents = log.safetyEvents.isNotEmpty()
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    suspend fun getSessionDetails(sessionId: String): SessionLog? {
        return withContext(Dispatchers.IO) {
            val file = File(sessionsDir, "session_$sessionId.json")
            if (file.exists()) {
                try {
                    val content = file.readText()
                    json.decodeFromString<SessionLog>(content)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    /**
     * Finds the most recent session left without an endTime (excluding the
     * session currently in progress) — evidence the service was killed.
     */
    suspend fun findInterruptedSession(): InterruptedSession? {
        return withContext(Dispatchers.IO) {
            val activeId = currentSession?.sessionId
            sessionsDir.listFiles()
                ?.sortedByDescending { it.lastModified() }
                ?.firstNotNullOfOrNull { file ->
                    try {
                        val log = json.decodeFromString<SessionLog>(file.readText())
                        if (log.endTime == null && log.sessionId != activeId) {
                            InterruptedSession(
                                sessionId = log.sessionId,
                                startTime = log.startTime,
                                lastActivityMs = file.lastModified()
                            )
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
        }
    }

    /** Closes an interrupted session file so it stops being reported. */
    suspend fun closeSession(sessionId: String, endTimeMs: Long) {
        withContext(Dispatchers.IO) {
            val file = File(sessionsDir, "session_$sessionId.json")
            if (file.exists()) {
                try {
                    val log = json.decodeFromString<SessionLog>(file.readText())
                    log.endTime = endTimeMs
                    file.writeText(json.encodeToString(SessionLog.serializer(), log))
                } catch (e: Exception) {
                    // Corrupt file — leave it; the 7-day purge will remove it
                }
            }
        }
    }

    suspend fun hasUnreviewedInterruption(): Boolean {
        return withContext(Dispatchers.IO) {
            currentSession?.interruptions?.isNotEmpty() == true
        }
    }

    suspend fun markInterruptionReviewed() {
        withContext(Dispatchers.IO) {
            currentSession?.let { session ->
                session.interruptions.clear()
                writeSessionToFile(session)
            }
        }
    }

    private fun writeSessionToFile(session: SessionLog) {
        val fileName = "session_${session.sessionId}.json"
        val file = File(sessionsDir, fileName)
        try {
            val content = json.encodeToString(SessionLog.serializer(), session)
            file.writeText(content)
        } catch (e: Exception) {
            // Log error but don't crash
        }
    }

    private fun purgeOldSessions() {
        val now = System.currentTimeMillis()
        val sevenDaysMs = 7L * 24 * 60 * 60 * 1000

        sessionsDir.listFiles()?.forEach { file ->
            val fileAge = now - file.lastModified()
            if (fileAge > sevenDaysMs) {
                file.delete()
            }
        }
    }
}
