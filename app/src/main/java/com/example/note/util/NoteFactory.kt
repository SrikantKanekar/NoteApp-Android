package com.example.note.util

import com.example.note.model.Note
import com.example.note.model.enums.NoteState
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteFactory @Inject constructor(
    private val dateUtil: DateUtil
) {

    fun createNote(
        id: String? = null,
        title: String? = null,
        body: String? = null,
        createdAt: String? = null,
        updatedAt: String? = null
    ): Note {
        return Note(
            id = id ?: UUID.randomUUID().toString(),
            title = title ?: "",
            body = body ?: "",
            createdAt = createdAt ?: dateUtil.getCurrentTimestamp(),
            updatedAt = updatedAt ?: dateUtil.getCurrentTimestamp(),
            state = NoteState.ACTIVE,
            pinned = false,
            color = 0,
            labels = listOf()
        )
    }

    fun createNotes(numNotes: Int): List<Note> {
        val list: ArrayList<Note> = ArrayList()
        for (i in 0 until numNotes) {
            list.add(
                createNote(
                    id = UUID.randomUUID().toString(),
                    title = UUID.randomUUID().toString(),
                    body = UUID.randomUUID().toString()
                )
            )
        }
        return list
    }

    fun createYesterdayNote(id: String? = null): Note {
        return createNote(
            id = id,
            createdAt = dateUtil.getYesterdayTimestamp(),
            updatedAt = dateUtil.getYesterdayTimestamp()
        )
    }
}
