package com.example.note.business.interactors.notelist

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeApiCall
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.*
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMultipleNotes(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
){

    // set true if an error occurs when deleting any of the notes from cache
    private var onDeleteError: Boolean = false

    /**
     * Logic:
     * 1. execute all the deletes and save result into an ArrayList<DataState<NoteListViewState>>
     * 2a. If one of the results is a failure, emit an "error" response
     * 2b. If all success, emit success response
     * 3. Update network with notes that were successfully deleted
     */
    fun deleteNotes(
        notes: List<Note>,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val successfulDeletes: ArrayList<Note> = ArrayList() // notes that were successfully deleted
        for(note in notes){
            val cacheResult = safeCacheCall(IO){
                noteCacheRepository.deleteNote(note.id)
            }

            val response = object: CacheResponseHandler<NoteListViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(result: Int): DataState<NoteListViewState>? {
                    if(result < 0){ // if error
                        onDeleteError = true
                    }
                    else{
                        successfulDeletes.add(note)
                    }
                    return null
                }
            }.getResult()

            // check for random errors
            if(response?.stateMessage?.response?.message
                    ?.contains(stateEvent.errorInfo()) == true){
                onDeleteError = true
            }

        }

        if(onDeleteError){
            emit(
                DataState.data<NoteListViewState>(
                    response = Response(
                        message = DELETE_NOTES_ERRORS,
                        uiType = UiType.Dialog,
                        messageType = MessageType.Success
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }
        else{
            emit(
                DataState.data<NoteListViewState>(
                    response = Response(
                        message = DELETE_NOTES_SUCCESS,
                        uiType = UiType.SnackBar,
                        messageType = MessageType.Success
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }

        updateNetwork(successfulDeletes)
    }

    private suspend fun updateNetwork(successfulDeletes: ArrayList<Note>){
        for (note in successfulDeletes){

            // insert into "deletes" node
            safeApiCall(IO){
                noteNetworkRepository.insertDeletedNote(note.id)
            }

            // delete from "notes" node
            safeApiCall(IO){
                noteNetworkRepository.deleteNote(note.id)
            }
        }
    }

    companion object{
        val DELETE_NOTES_SUCCESS = "Successfully deleted notes."
        val DELETE_NOTES_ERRORS = "Not all the notes you selected were deleted. There was some errors."
        val DELETE_NOTES_YOU_MUST_SELECT = "You haven't selected any notes to delete."
        val DELETE_NOTES_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }
}













