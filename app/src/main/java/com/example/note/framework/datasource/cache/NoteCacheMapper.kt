package com.example.note.framework.datasource.cache

import com.example.note.business.domain.model.Note
import com.example.note.business.domain.util.DomainMapper

class NoteCacheMapper : DomainMapper<NoteEntity, Note> {

    fun entityListToNoteList(entities: List<NoteEntity>): List<Note> {
        val list: ArrayList<Note> = ArrayList()
        for (entity in entities) {
            list.add(mapToDomainModel(entity))
        }
        return list
    }

    fun noteListToEntityList(notes: List<Note>): List<NoteEntity> {
        val entities: ArrayList<NoteEntity> = ArrayList()
        for (note in notes) {
            entities.add(mapFromDomainModel(note))
        }
        return entities
    }

    override fun mapToDomainModel(model: NoteEntity): Note {
        return Note(
            id = model.id,
            title = model.title,
            body = model.body,
            updated_at = model.updated_at,
            created_at = model.created_at
        )
    }

    override fun mapFromDomainModel(domainModel: Note): NoteEntity {
        return NoteEntity(
            id = domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
            updated_at = domainModel.updated_at,
            created_at = domainModel.created_at
        )
    }
}
