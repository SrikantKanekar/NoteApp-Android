package com.example.note.business.interactors.notelist

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.util.CacheResponseHandler
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.*
import com.example.note.framework.datasource.cache.NOTE_FILTER_DATE_CREATED
import com.example.note.framework.datasource.cache.NOTE_ORDER_DESC
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchNotes(
    private val noteCacheRepository: NoteCacheRepository
) {

    fun searchNotes(
        query: String = "",
        filterAndOrder: String = NOTE_ORDER_DESC + NOTE_FILTER_DATE_CREATED,
        page: Int = 1,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {
        var updatedPage = page
        if (page <= 0) {
            updatedPage = 1
        }
        val cacheResult = safeCacheCall(Dispatchers.IO) {
            noteCacheRepository.searchNotes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<NoteListViewState, List<Note>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(result: List<Note>): DataState<NoteListViewState> {

                var message: String? = SEARCH_NOTES_SUCCESS
                var uiComponentType: UiType? = UiType.None
                if (result.isEmpty()) {
                    message = SEARCH_NOTES_NO_MATCHING_RESULTS
                    uiComponentType = UiType.SnackBar
                }

                return DataState.data(
                    response = Response(
                        message = message,
                        uiType = uiComponentType as UiType,
                        messageType = MessageType.Success
                    ),
                    data = NoteListViewState(
                        noteList = ArrayList(result)
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
        val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
        val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."

    }
}







