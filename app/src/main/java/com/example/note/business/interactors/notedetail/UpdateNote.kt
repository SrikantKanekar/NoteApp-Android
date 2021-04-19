package com.example.note.business.interactors.notedetail

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
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {

    fun execute(
        note: Note,
        stateEvent: StateEvent
    ): Flow<DataState<NoteDetailViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            noteCacheRepository.updateNote(
                id = note.id,
                newTitle = note.title,
                newBody = note.body,
                timestamp = null // generate new timestamp
            )
        }

        val cacheResponse = object : CacheResponseHandler<NoteDetailViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(result: Int): DataState<NoteDetailViewState> {
                return if (result > 0) {
                    DataState.data(
                        response = Response(
                            message = "Successfully updated note",
                            uiType = SnackBar,
                            messageType = Success
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = "Failed to update note",
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
            safeApiCall(Dispatchers.IO) {
                val networkResponse = noteNetworkRepository.insertOrUpdateNote(note)
                printServerResponse("insertOrUpdateNote", networkResponse)
            }
        }
    }
}