package com.example.note.di

import com.example.note.business.data.cache.NoteRepository
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.interactors.common.DeleteNote
import com.example.note.business.interactors.noteList.GetNumNotes
import com.example.note.business.interactors.noteList.InsertNewNote
import com.example.note.business.interactors.noteList.NoteListInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @Provides
    fun provideNoteListInteractors(
        noteRepository: NoteRepository,
        noteFactory: NoteFactory
    ): NoteListInteractors {
        return NoteListInteractors(
            InsertNewNote(noteRepository, noteFactory),
            DeleteNote(noteRepository),
            GetNumNotes(noteRepository)
        )
    }
}