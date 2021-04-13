package com.example.note.business.interactors.notelist

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeApiCall
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.domain.state.*
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertNewNote(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository,
    private val noteFactory: NoteFactory
){

    fun insertNewNote(
        id: String? = null,
        title: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val newNote = noteFactory.createSingleNote(
            id = id ?: UUID.randomUUID().toString(),
            title = title,
            body = ""
        )
        val cacheResult = safeCacheCall(IO){
            noteCacheRepository.insertNote(newNote)
        }

        val cacheResponse = object: CacheResponseHandler<NoteListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Long): DataState<NoteListViewState>? {
                return if(resultObj > 0){
                    val viewState =
                        NoteListViewState(
                            newNote = newNote
                        )
                    DataState.data(
                        response = Response(
                            message = INSERT_NOTE_SUCCESS,
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                }
                else{
                    DataState.data(
                        response = Response(
                            message = INSERT_NOTE_FAILED,
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)

        updateNetwork(cacheResponse?.stateMessage?.response?.message, newNote)
    }

    private suspend fun updateNetwork(cacheResponse: String?, newNote: Note ){
        if(cacheResponse.equals(INSERT_NOTE_SUCCESS)){

            safeApiCall(IO){
                noteNetworkRepository.insertOrUpdateNote(newNote)
            }
        }
    }

    companion object{
        val INSERT_NOTE_SUCCESS = "Successfully inserted new note."
        val INSERT_NOTE_FAILED = "Failed to insert new note."
    }
}