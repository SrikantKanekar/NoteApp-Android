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

    override suspend fun insertNotes(notes: List<Note>) {
        noteCacheDataSource.insertNotes(notes)
        noteNetworkDataSource.insertOrUpdateNotes(notes)
    }

    override suspend fun updateNotes(notes: List<Note>) {
        val now = dateUtil.getCurrentTimestamp()
        val updatedNotes = notes.map { it.copy(updatedAt = now) }
        noteCacheDataSource.updateNotes(updatedNotes)
        noteNetworkDataSource.insertOrUpdateNotes(updatedNotes)
    }

    override suspend fun deleteNotes(notes: List<Note>) {
        val ids = notes.map { it.id }
        noteCacheDataSource.deleteNotes(ids)
        noteNetworkDataSource.deleteNotes(ids)
    }

    override suspend fun getNote(id: String): Note {
        val result = noteCacheDataSource.getNote(id)
        return result ?: throw Exception("Note not found")
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteCacheDataSource.getAllNotes()
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
                    when (val cachedNote = noteCacheDataSource.getNote(networkNote.id)) {
                        null -> cacheInsert.add(networkNote)
                        else -> {
                            cachedNotes.remove(cachedNote)
                            val cacheUpdatedAt = cachedNote.updatedAt
                            val networkUpdatedAt = networkNote.updatedAt

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
}