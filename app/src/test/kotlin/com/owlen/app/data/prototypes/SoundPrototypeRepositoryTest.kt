package com.owlen.app.data.prototypes

import com.owlen.app.domain.model.SoundPrototype
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class SoundPrototypeRepositoryTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private fun repository() = SoundPrototypeRepository(tempFolder.root)

    private fun prototype(id: String, name: String = "Door") = SoundPrototype(
        id = id,
        name = name,
        embedding = FloatArray(1024) { it * 0.001f },
        threshold = 0.82f,
        sampleCount = 3,
        createdAtMs = 1234567890L
    )

    @Test
    fun testLoadWithNoFileReturnsEmptyList() = runBlocking {
        val repo = repository()
        repo.load()
        assertTrue(repo.prototypes.value.isEmpty())
    }

    @Test
    fun testAddAndReloadRoundTripsFullEmbedding() = runBlocking {
        val original = prototype("a")
        repository().add(original)

        // Fresh instance reads from disk
        val repo = repository()
        repo.load()
        val loaded = repo.prototypes.value
        assertEquals(1, loaded.size)
        assertEquals(original, loaded[0])
        assertEquals(1024, loaded[0].embedding.size)
    }

    @Test
    fun testDeleteRemovesPrototype() = runBlocking {
        val repo = repository()
        repo.add(prototype("a"))
        repo.add(prototype("b", name = "Fridge"))
        repo.delete("a")

        assertEquals(listOf("b"), repo.prototypes.value.map { it.id })

        // Deletion is persisted
        val fresh = repository()
        fresh.load()
        assertEquals(listOf("b"), fresh.prototypes.value.map { it.id })
    }

    @Test
    fun testAddWithSameIdReplaces() = runBlocking {
        val repo = repository()
        repo.add(prototype("a", name = "Old"))
        repo.add(prototype("a", name = "New"))
        assertEquals(1, repo.prototypes.value.size)
        assertEquals("New", repo.prototypes.value[0].name)
    }

    @Test
    fun testCorruptFileLoadsAsEmptyList() = runBlocking {
        File(tempFolder.root, SoundPrototypeRepository.FILE_NAME)
            .writeText("{ not valid json !!!")
        val repo = repository()
        repo.load()
        assertTrue(repo.prototypes.value.isEmpty())
    }

    @Test
    fun testUnknownJsonKeysAreIgnored() = runBlocking {
        // Forward-compat: a newer app version may add fields
        File(tempFolder.root, SoundPrototypeRepository.FILE_NAME).writeText(
            """[{"id":"a","name":"Door","embedding":[0.1,0.2],"threshold":0.8,""" +
                """"sampleCount":3,"createdAtMs":1,"futureField":"x"}]"""
        )
        val repo = repository()
        repo.load()
        assertEquals(1, repo.prototypes.value.size)
        assertEquals("a", repo.prototypes.value[0].id)
    }
}
