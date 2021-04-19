package com.example.note.business.interactors.notedetail

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.*
import com.example.note.framework.presentation.ui.noteDetail.state.NoteDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNote(
    private val noteCacheRepository: NoteCacheRepository
){

    fun execute(
        id: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteDetailViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteCacheRepository.getNote(id)
        }

        val response = object: CacheResponseHandler<NoteDetailViewState, Note?>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(result: Note?): DataState<NoteDetailViewState> {
                return if(result != null){
                    DataState.data(
                        data = NoteDetailViewState(note = result),
                        response = Response(
                            message = GET_NOTE_SUCCESS,
                            uiType = UiType.None,
                            messageType = MessageType.Success
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    DataState.data(
                        data = null,
                        response = Response(
                            message = GET_NOTE_FAILED,
                            uiType = UiType.SnackBar,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_NOTE_SUCCESS = "Successfully displayed note on detail screen"
        val GET_NOTE_FAILED = "Failed to display note."

    }
}