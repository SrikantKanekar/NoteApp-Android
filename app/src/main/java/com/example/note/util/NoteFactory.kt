package com.example.note.util

import com.example.note.model.Note
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
        created_at: String? = null,
        updated_at: String? = null
    ): Note {
        return Note(
            id = id ?: UUID.randomUUID().toString(),
            title = title ?: "",
            body = body ?: "",
            created_at = created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = updated_at ?: dateUtil.getCurrentTimestamp()
        )
    }

    fun createNotes(numNotes: Int): List<Note> {
        val list: ArrayList<Note> = ArrayList()
        for (i in 0 until numNotes) { // exclusive on upper bound
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
            created_at = dateUtil.getYesterdayTimestamp(),
            updated_at = dateUtil.getYesterdayTimestamp()
        )
    }
}
