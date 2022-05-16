package com.example.note.business.interactors.common

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeApiCall
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType.Error
import com.example.note.business.domain.state.MessageType.Success
import com.example.note.business.domain.state.Response
import com.example.note.business.domain.state.StateEvent
import com.example.note.business.domain.state.UiType.SnackBar
import com.example.note.business.domain.util.printServerResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNote<ViewState>(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {

    fun execute(
        id: String,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO) {
            noteCacheRepository.deleteNote(id)
        }

        val cacheResponse = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(result: Int): DataState<ViewState> {
                return if (result > 0) {
                    DataState.data(
                        response = Response(
                            message = "Successfully deleted note",
                            uiType = SnackBar, //send undo snackBar here
                            messageType = Success
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = "Failed to delete note",
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

            // insert into 'deletes' node
            safeApiCall(IO) {
                val networkResponse = noteNetworkRepository.insertDeletedNote(id)
                printServerResponse("insertDeletedNote", networkResponse)
            }

            // delete from 'notes' node
            safeApiCall(IO) {
                val networkResponse = noteNetworkRepository.deleteNote(id)
                printServerResponse("deleteNote", networkResponse)
            }
        }
    }
}













