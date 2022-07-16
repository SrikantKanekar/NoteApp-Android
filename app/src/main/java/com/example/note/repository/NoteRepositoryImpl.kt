package com.example.note.repository

import com.example.note.cache.database.dataSource.NoteCacheDataSource
import com.example.note.model.Note
import com.example.note.network.dataSource.NoteNetworkDataSource
import com.example.note.util.DateUtil
import com.example.note.util.printLogD
import com.example.note.util.safeApiCall
import com.example.note.util.safeCacheCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val dateUtil: DateUtil
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        noteCacheDataSource.insertNote(note)
        noteNetworkDataSource.insertOrUpdateNote(note)
    }

    override suspend fun updateNote(note: Note) {
        val updatedNote = note.copy(updated_at = dateUtil.getCurrentTimestamp())
        noteCacheDataSource.updateNote(updatedNote)
        noteNetworkDataSource.insertOrUpdateNote(updatedNote)
    }

    override suspend fun getNote(id: String): Note {
        val result = noteCacheDataSource.getNote(id)
        return result ?: throw Exception("Note not found")
    }

    override fun searchNotes(
        query: String?,
        filterAndOrder: String,
        page: Int
    ): Flow<List<Note>> {
        return noteCacheDataSource.searchNotes(
            query = query ?: "",
            filterAndOrder = filterAndOrder,
            page = page
        )
    }

    /**
     * get all notes from network.
     * if they do not exist in cache, insert them.
     * if they do exist in cache, make sure they are up to date.
     * while looping, remove notes from the cachedNotes list.
     * If any remain, it means they should be in the network but aren't. So insert them.
     **/
    override suspend fun syncNotes() {
        withContext(IO) {
            val cacheInsert = ArrayList<Note>()
            val cacheUpdate = ArrayList<Note>()
            val networkInsert = ArrayList<Note>()
            val networkUpdate = ArrayList<Note>()

            val networkNotes = noteNetworkDataSource.getAllNotes()
            val cachedNotes = ArrayList(noteCacheDataSource.getAllNotes())

            for (networkNote in networkNotes) {
                try {
                    val cachedNote = noteCacheDataSource.getNote(networkNote.id)
                    when (cachedNote) {
                        null -> cacheInsert.add(networkNote)
                        else -> {
                            cachedNotes.remove(cachedNote)
                            val cacheUpdatedAt = cachedNote.updated_at
                            val networkUpdatedAt = networkNote.updated_at

                            when {
                                networkUpdatedAt > cacheUpdatedAt -> cacheUpdate.add(networkNote)
                                networkUpdatedAt < cacheUpdatedAt -> networkUpdate.add(cachedNote)
                            }
                        }
                    }
                } catch (e: Exception) {
                    printLogD("syncNotes", e.message.toString())
                }
            }

            networkInsert.addAll(cachedNotes)

            safeCacheCall(IO) { noteCacheDataSource.insertNotes(cacheInsert) }
            safeCacheCall(IO) { noteCacheDataSource.updateNotes(cacheUpdate) }
            safeApiCall(IO) {
                noteNetworkDataSource.insertOrUpdateNotes(networkInsert + networkUpdate)
            }
        }
    }

    override suspend fun deleteNote(note: Note) {
        noteCacheDataSource.updateNote(note.copy(deleted = true))
        noteNetworkDataSource.insertOrUpdateNote(note.copy(deleted = true))
    }

    override suspend fun deleteNotes(notes: List<Note>) {
        val updatedNotes = notes.map { it.copy(deleted = true) }
        noteCacheDataSource.updateNotes(updatedNotes)
        noteNetworkDataSource.insertOrUpdateNotes(updatedNotes)
    }

    override suspend fun restoreDeletedNote(note: Note) {
        noteCacheDataSource.updateNote(note.copy(deleted = false))
        noteNetworkDataSource.insertOrUpdateNote(note.copy(deleted = false))
    }
}