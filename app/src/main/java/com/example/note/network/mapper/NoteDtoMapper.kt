package com.example.note.network.mapper

import com.example.note.model.Note
import com.example.note.model.enums.NoteState.ACTIVE
import com.example.note.network.dto.NoteDto
import com.example.note.util.Mapper
import javax.inject.Inject

class NoteDtoMapper @Inject constructor() : Mapper<NoteDto, Note> {

    override fun toModel(obj: NoteDto): Note {
        return Note(
            id = obj.id,
            title = obj.title,
            body = obj.body,
            updated_at = obj.updated_at,
            created_at = obj.created_at,
            state = obj.state ?: ACTIVE
        )
    }

    override fun fromModel(model: Note): NoteDto {
        return NoteDto(
            id = model.id,
            title = model.title,
            body = model.body,
            updated_at = model.updated_at,
            created_at = model.created_at,
            state = model.state
        )
    }
}
