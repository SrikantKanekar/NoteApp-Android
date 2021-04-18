package com.example.note.business.interactors.splash

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.ApiResponseHandler
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeApiCall
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.util.printLogD
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
Query all notes in the cache. It will then search server for
each corresponding note but with an extra filter: It will only return notes where
cached_note.updated_at < network_note.updated_at. It will update the cached notes
where that condition is met. If the note does not exist in Firestore (maybe due to
network being down at time of insertion), insert it
(**This must be done AFTER
checking for deleted notes and performing that sync**).
 **/
class SyncNotes(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {
    private var cacheInsert = 0
    private var cacheUpdate = 0
    private var networkInsert = 0
    private var networkUpdate = 0

    suspend fun syncNotes() {

        val cachedNotesList = getCachedNotes()

        val networkNotesList = getNetworkNotes()

        syncNetworkNotesWithCachedNotes(
            ArrayList(cachedNotesList),
            networkNotesList
        )

        printLogD("SyncNotes", "Cache -> $cacheInsert inserted and $cacheUpdate updated")
        printLogD("SyncNotes", "Server -> $networkInsert inserted and $networkUpdate updated")
    }

    private suspend fun getCachedNotes(): List<Note> {
        val cacheResult = safeCacheCall(IO) {
            noteCacheRepository.getAllNotes()
        }

        val response = object : CacheResponseHandler<List<Note>, List<Note>>(
            response = cacheResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(result: List<Note>): DataState<List<Note>> {
                return DataState.data(
                    response = null,
                    data = result,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }

    private suspend fun getNetworkNotes(): List<Note> {
        val networkResult = safeApiCall(IO) {
            noteNetworkRepository.getAllNotes()
        }

        val response = object : ApiResponseHandler<List<Note>, List<Note>>(
            response = networkResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(result: List<Note>): DataState<List<Note>> {
                return DataState.data(
                    response = null,
                    data = result,
                    stateEvent = null
                )
            }
        }.getResult()

        return response?.data ?: ArrayList()
    }

    /**
     * get all notes from network.
     * if they do not exist in cache, insert them.
     * if they do exist in cache, make sure they are up to date.
     * while looping, remove notes from the cachedNotes list.
     * If any remain, it means they should be in the network but aren't. So insert them.
     **/
    private suspend fun syncNetworkNotesWithCachedNotes(
        cachedNotes: ArrayList<Note>,
        networkNotes: List<Note>
    ) = withContext(IO) {

        for (note in networkNotes) {
            val cachedNote = noteCacheRepository.searchNoteById(note.id)
            if (cachedNote != null) {
                cachedNotes.remove(cachedNote)
                checkIfCachedNoteRequiresUpdate(cachedNote, note)
            } else {
                noteCacheRepository.insertNote(note)
                cacheInsert++
            }
        }

        // insert remaining into network
        for (cachedNote in cachedNotes) {
            val networkResponse = noteNetworkRepository.insertOrUpdateNote(cachedNote)
            if (networkResponse.successful) networkInsert++
        }
    }

    private suspend fun checkIfCachedNoteRequiresUpdate(
        cachedNote: Note,
        networkNote: Note
    ) {
        val cacheUpdatedAt = cachedNote.updated_at
        val networkUpdatedAt = networkNote.updated_at

        // update cache (network has newest data)
        if (networkUpdatedAt > cacheUpdatedAt) {
            cacheUpdate++
            safeCacheCall(IO) {
                noteCacheRepository.updateNote(
                    networkNote.id,
                    networkNote.title,
                    networkNote.body,
                    networkNote.updated_at
                )
            }
        }

        // update network (cache has newest data)
        else if (networkUpdatedAt < cacheUpdatedAt) {
            safeApiCall(IO) {
                val networkResponse = noteNetworkRepository.insertOrUpdateNote(cachedNote)
                if (networkResponse.successful) networkUpdate++
            }
        }
    }
}