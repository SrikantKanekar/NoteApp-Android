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
            updated_at = obj.updated_at,
            created_at = obj.created_at,
            state = obj.state
        )
    }

    override fun fromModel(model: Note): NoteEntity {
        return NoteEntity(
            id = model.id,
            title = model.title,
            body = model.body,
            updated_at = model.updated_at,
            created_at = model.created_at,
            state = model.state
        )
    }
}
