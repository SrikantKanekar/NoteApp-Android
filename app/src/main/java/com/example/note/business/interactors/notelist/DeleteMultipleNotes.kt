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
import com.example.note.business.domain.state.UiType.Dialog
import com.example.note.business.domain.state.UiType.SnackBar
import com.example.note.business.domain.util.printServerResponse
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMultipleNotes(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {

    // set true if an error occurs when deleting any of the notes from cache
    private var onDeleteError: Boolean = false

    /**
     * Logic:
     * 1. execute all the deletes and save result into an ArrayList<DataState<NoteListViewState>>
     * 2. a) If one of the results is a failure, emit an "error" response. b) If all success, emit success response
     * 3. Update network with notes that were successfully deleted
     */
    fun execute(
        notes: List<Note>,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val successfulDeletes: ArrayList<Note> = ArrayList() // notes that were successfully deleted
        for (note in notes) {
            val cacheResult = safeCacheCall(IO) {
                noteCacheRepository.deleteNote(note.id)
            }

            val cacheResponse = object : CacheResponseHandler<NoteListViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(result: Int): DataState<NoteListViewState>? {
                    if (result < 0) {
                        onDeleteError = true
                    } else {
                        successfulDeletes.add(note)
                    }
                    return null
                }
            }.getResult()

            // check for random errors
            if (cacheResponse?.stateMessage?.response?.messageType == Error) {
                onDeleteError = true
            }
        }

        if (onDeleteError) {
            emit(
                DataState.data(
                    response = Response(
                        message = "Not all the notes you selected were deleted. There was some errors",
                        uiType = Dialog,
                        messageType = Error
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        } else {
            emit(
                DataState.data(
                    response = Response(
                        message = "Successfully deleted all notes",
                        uiType = SnackBar,
                        messageType = Success
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }

        for (note in successfulDeletes) {

            // insert into "deletes" node
            safeApiCall(IO) {
                val networkResponse = noteNetworkRepository.insertDeletedNote(note.id)
                printServerResponse("insertDeletedNote", networkResponse)
            }

            // delete from "notes" node
            safeApiCall(IO) {
                val networkResponse = noteNetworkRepository.deleteNote(note.id)
                printServerResponse("deleteNote", networkResponse)
            }
        }
    }
}