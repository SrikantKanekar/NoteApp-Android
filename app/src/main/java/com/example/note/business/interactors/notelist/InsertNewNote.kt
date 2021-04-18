package com.example.note.business.interactors.notelist

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeApiCall
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType.Error
import com.example.note.business.domain.state.MessageType.Success
import com.example.note.business.domain.state.Response
import com.example.note.business.domain.state.StateEvent
import com.example.note.business.domain.state.UiType.SnackBar
import com.example.note.business.domain.util.printServerResponse
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertNewNote(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {

    fun execute(
        note: Note,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val cacheResult = safeCacheCall(IO) {
            noteCacheRepository.insertNote(note)
        }

        val cacheResponse = object : CacheResponseHandler<NoteListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(result: Long): DataState<NoteListViewState> {
                return if (result > 0) {
                    DataState.data(
                        response = Response(
                            message = "Successfully inserted new note.",
                            uiType = SnackBar,
                            messageType = Success
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = "Failed to insert new note.",
                            uiType = SnackBar,
                            messageType = Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)

        if (cacheResponse?.stateMessage?.response?.messageType == Success) {
            safeApiCall(IO) {
                val networkResponse = noteNetworkRepository.insertOrUpdateNote(note)
                printServerResponse("insertOrUpdateNote", networkResponse)
            }
        }
    }
}