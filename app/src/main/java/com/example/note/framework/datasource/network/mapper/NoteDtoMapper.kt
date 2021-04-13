package com.example.note.framework.datasource.network.mapper

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DomainMapper
import com.example.note.framework.datasource.network.dto.NoteDto

class NoteDtoMapper : DomainMapper<NoteDto, Note> {

    fun dtoListToNoteList(dtoList: List<NoteDto>): List<Note> {
        val list: ArrayList<Note> = ArrayList()
        for (dto in dtoList) {
            list.add(mapToDomainModel(dto))
        }
        return list
    }

    fun noteListToDtoList(notes: List<Note>): List<NoteDto> {
        val entities: ArrayList<NoteDto> = ArrayList()
        for (note in notes) {
            entities.add(mapFromDomainModel(note))
        }
        return entities
    }

    override fun mapToDomainModel(model: NoteDto): Note {
        return Note(
            id = model.id,
            title = model.title,
            body = model.body,
            updated_at = model.updated_at,
            created_at = model.created_at
        )
    }

    override fun mapFromDomainModel(domainModel: Note): NoteDto {
        return NoteDto(
            id = domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
            updated_at = domainModel.updated_at,
            created_at = domainModel.created_at
        )
    }
}
