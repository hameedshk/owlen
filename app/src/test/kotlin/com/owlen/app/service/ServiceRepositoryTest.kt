package com.owlen.app.service

import com.owlen.app.domain.model.MaskingSound
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ServiceRepositoryTest {
    private lateinit var repository: ServiceRepository

    @Before
    fun setUp() {
        repository = ServiceRepository()
    }

    @Test
    fun initialState_isIdle() {
        runBlocking {
            val state = repository.state.first()
            assertIs<ServiceState.Idle>(state)
        }
    }

    @Test
    fun updateState_emitsNewStateToFlow() {
        runBlocking {
            val newState = ServiceState.Monitoring
            repository.updateState(newState)

            val state = repository.state.first()
            assertIs<ServiceState.Monitoring>(state)
        }
    }

    @Test
    fun updateSessionStartTime_emitsCorrectValue() {
        runBlocking {
            val timeMs = System.currentTimeMillis()
            repository.updateSessionStartTime(timeMs)

            val sessionTime = repository.sessionStartTime.first()
            assertEquals(timeMs, sessionTime)
        }
    }

    @Test
    fun updateSessionStartTime_toNull_emitsNull() {
        runBlocking {
            repository.updateSessionStartTime(System.currentTimeMillis())
            repository.updateSessionStartTime(null)

            val sessionTime = repository.sessionStartTime.first()
            assertNull(sessionTime)
        }
    }

    @Test
    fun updateState_withMaskingActive() {
        runBlocking {
            val state = ServiceState.MaskingActive(MaskingSound.PINK_NOISE, 0.5f)
            repository.updateState(state)

            val emittedState = repository.state.first()
            assertIs<ServiceState.MaskingActive>(emittedState)
            assertEquals(MaskingSound.PINK_NOISE, emittedState.sound)
            assertEquals(0.5f, emittedState.volume)
        }
    }

    @Test
    fun updateState_multipleTimes_emitsLatest() {
        runBlocking {
            repository.updateState(ServiceState.Monitoring)
            repository.updateState(ServiceState.Idle)
            repository.updateState(ServiceState.Monitoring)

            val state = repository.state.first()
            assertIs<ServiceState.Monitoring>(state)
        }
    }
}
