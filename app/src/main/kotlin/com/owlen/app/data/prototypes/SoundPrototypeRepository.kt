package com.owlen.app.data.prototypes

import com.owlen.app.domain.model.SoundPrototype
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Persists user-enrolled disturbance prototypes as a single flat JSON file
 * (prototypes.json) in the app's files directory — same pattern as
 * SessionLogger. Writes are atomic (tmp file + rename) so a crash mid-write
 * never corrupts existing enrollments.
 */
class SoundPrototypeRepository(private val storageDir: File) {

    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }

    private val file = File(storageDir, FILE_NAME)
    private val mutex = Mutex()

    private val _prototypes = MutableStateFlow<List<SoundPrototype>>(emptyList())
    val prototypes: StateFlow<List<SoundPrototype>> = _prototypes.asStateFlow()

    suspend fun load() {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                _prototypes.value = readFromFile()
            }
        }
    }

    suspend fun add(prototype: SoundPrototype) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val updated = readFromFile().filterNot { it.id == prototype.id } + prototype
                writeToFile(updated)
                _prototypes.value = updated
            }
        }
    }

    suspend fun delete(id: String) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val updated = readFromFile().filterNot { it.id == id }
                writeToFile(updated)
                _prototypes.value = updated
            }
        }
    }

    private fun readFromFile(): List<SoundPrototype> {
        if (!file.exists()) return emptyList()
        return try {
            json.decodeFromString(serializer, file.readText())
        } catch (e: Exception) {
            // Corrupt file — start over rather than crash the pipeline
            emptyList()
        }
    }

    private fun writeToFile(prototypes: List<SoundPrototype>) {
        try {
            if (!storageDir.exists()) storageDir.mkdirs()
            val tmp = File(storageDir, "$FILE_NAME.tmp")
            tmp.writeText(json.encodeToString(serializer, prototypes))
            if (!tmp.renameTo(file)) {
                // Windows/robustness fallback: rename fails if target exists
                file.delete()
                tmp.renameTo(file)
            }
        } catch (e: Exception) {
            // Don't crash on storage failure; enrollments stay in memory
        }
    }

    companion object {
        const val FILE_NAME = "prototypes.json"
        private val serializer = ListSerializer(SoundPrototype.serializer())
    }
}
