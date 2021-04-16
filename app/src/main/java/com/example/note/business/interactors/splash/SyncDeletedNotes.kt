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

/**
Search server for all notes in the "deleted" node.
It will then search the cache for notes matching those deleted notes.
If a match is found, it is deleted from the cache.
 **/
class SyncDeletedNotes(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {

    suspend fun syncDeletedNotes() {

        printLogD("", "Started")
        val apiResult = safeApiCall(IO) {
            noteNetworkRepository.getDeletedNotes()
        }
        val response = object : ApiResponseHandler<List<Note>, List<Note>>(
            response = apiResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(result: List<Note>): DataState<List<Note>> {
                return DataState.data(
                    response = null,
                    data = result,
                    stateEvent = null
                )
            }
        }

        val deletedNotes = response.getResult()?.data ?: ArrayList()
        printLogD("SyncDeletedNotes", "Retrieved ${deletedNotes.size} deleted notes from server")

        val cacheResult = safeCacheCall(IO) {
            noteCacheRepository.deleteNotes(deletedNotes)
        }

        object : CacheResponseHandler<Int, Int>(
            response = cacheResult,
            stateEvent = null
        ) {
            override suspend fun handleSuccess(result: Int): DataState<Int> {
                printLogD("SyncDeletedNotes", "deleted $result notes from cache")
                return DataState.data(
                    response = null,
                    data = result,
                    stateEvent = null
                )
            }
        }.getResult()
    }
}