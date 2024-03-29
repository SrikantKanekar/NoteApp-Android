package com.example.note.cache.database.mapper

import com.example.note.cache.database.entity.NoteEntity
import com.example.note.model.Note
import com.example.note.util.Mapper
import javax.inject.Inject

class NoteEntityMapper @Inject constructor() : Mapper<NoteEntity, Note> {

    override fun toModel(obj: NoteEntity): Note {
        return Note(
            id = obj.id,
            title = obj.title,
            body = obj.body,
            updatedAt = obj.updatedAt,
            createdAt = obj.createdAt,
            state = obj.state,
            pinned = obj.pinned,
            color = obj.color,
            labels = if (obj.labels.isNotEmpty()) obj.labels.split(", ") else emptyList()
        )
    }

    override fun fromModel(model: Note): NoteEntity {
        return NoteEntity(
            id = model.id,
            title = model.title,
            body = model.body,
            updatedAt = model.updatedAt,
            createdAt = model.createdAt,
            state = model.state,
            pinned = model.pinned,
            color = model.color,
            labels = model.labels.joinToString()
        )
    }
}
