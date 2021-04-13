package com.example.note.business.interactors.noteList

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.state.*
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumNotes(
    private val noteRepository: NoteCacheRepository
){

    fun getNumNotes(
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            noteRepository.getNumNotes()
        }
        val response =  object: CacheResponseHandler<NoteListViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<NoteListViewState>? {
                val viewState = NoteListViewState(
                    numNotesInCache = resultObj
                )
                return DataState.data(
                    response = Response(
                        message = GET_NUM_NOTES_SUCCESS,
                        uiType = UiType.None,
                        messageType = MessageType.Success
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_NUM_NOTES_SUCCESS = "Successfully retrieved the number of notes from the cache."
        val GET_NUM_NOTES_FAILED = "Failed to get the number of notes from the cache."
    }
}