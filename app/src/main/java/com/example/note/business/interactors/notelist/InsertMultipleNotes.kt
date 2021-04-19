package com.example.note.business.interactors.notelist

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.data.util.safeApiCall
import com.example.note.business.data.util.safeCacheCall
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.state.DataState
import com.example.note.business.domain.state.MessageType.Info
import com.example.note.business.domain.state.Response
import com.example.note.business.domain.state.StateEvent
import com.example.note.business.domain.state.UiType.None
import com.example.note.business.domain.util.DateUtil
import com.example.note.business.domain.util.printServerResponse
import com.example.note.framework.presentation.ui.noteList.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// For testing
class InsertMultipleNotes(
    private val noteCacheRepository: NoteCacheRepository,
    private val noteNetworkRepository: NoteNetworkRepository
) {

    fun execute(
        numNotes: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val noteList = NoteListTester.generateNoteList(numNotes)
        safeCacheCall(IO) {
            noteCacheRepository.insertNotes(noteList)
        }

        emit(
            DataState.data<NoteListViewState>(
                response = Response(
                    message = "success",
                    uiType = None,
                    messageType = Info
                ),
                data = null,
                stateEvent = stateEvent
            )
        )

        safeApiCall(IO) {
            val networkResponse = noteNetworkRepository.insertOrUpdateNotes(noteList)
            printServerResponse("insertOrUpdateNotes", networkResponse)
        }
    }
}


private object NoteListTester {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val dateUtil = DateUtil(dateFormat)

    fun generateNoteList(numNotes: Int): List<Note> {
        val list: ArrayList<Note> = ArrayList()
        for (id in 0..numNotes) {
            list.add(generateNote())
        }
        return list
    }

    fun generateNote(): Note {
        return Note(
            id = UUID.randomUUID().toString(),
            title = UUID.randomUUID().toString(),
            body = UUID.randomUUID().toString(),
            created_at = dateUtil.getCurrentTimestamp(),
            updated_at = dateUtil.getCurrentTimestamp()
        )
    }
}