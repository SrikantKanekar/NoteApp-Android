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
            updatedAt = obj.updatedAt,
            createdAt = obj.createdAt,
            state = obj.state ?: ACTIVE,
            pinned = obj.pinned ?: false,
            color = obj.color ?: 0,
            labels = obj.labels ?: listOf()
        )
    }

    override fun fromModel(model: Note): NoteDto {
        return NoteDto(
            id = model.id,
            title = model.title,
            body = model.body,
            updatedAt = model.updatedAt,
            createdAt = model.createdAt,
            state = model.state,
            pinned = model.pinned,
            color = model.color,
            labels = model.labels
        )
    }
}
