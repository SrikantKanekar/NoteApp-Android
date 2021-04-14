package com.example.note.di

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.business.data.network.NoteNetworkRepository
import com.example.note.business.domain.model.NoteFactory
import com.example.note.business.interactors.common.DeleteNote
import com.example.note.business.interactors.notedetail.NoteDetailInteractors
import com.example.note.business.interactors.notedetail.UpdateNote
import com.example.note.business.interactors.notelist.*
import com.example.note.business.interactors.splash.NoteSyncInteractors
import com.example.note.business.interactors.splash.SyncDeletedNotes
import com.example.note.business.interactors.splash.SyncNotes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @Provides
    fun provideNoteListInteractors(
        noteCacheRepository: NoteCacheRepository,
        noteNetworkRepository: NoteNetworkRepository,
        noteFactory: NoteFactory
    ): NoteListInteractors {
        return NoteListInteractors(
            InsertNewNote(noteCacheRepository, noteNetworkRepository, noteFactory),
            InsertMultipleNotes(noteCacheRepository, noteNetworkRepository),
            DeleteNote(noteCacheRepository, noteNetworkRepository),
            SearchNotes(noteCacheRepository),
            GetNumNotes(noteCacheRepository),
            RestoreDeletedNote(noteCacheRepository, noteNetworkRepository),
            DeleteMultipleNotes(noteCacheRepository, noteNetworkRepository)
        )
    }

    @Provides
    fun provideNoteDetailInteractors(
        noteCacheRepository: NoteCacheRepository,
        noteNetworkRepository: NoteNetworkRepository
    ): NoteDetailInteractors {
        return NoteDetailInteractors(
            DeleteNote(noteCacheRepository, noteNetworkRepository),
            UpdateNote(noteCacheRepository, noteNetworkRepository)
        )
    }

    @Provides
    fun provideNoteSyncInteractors(
        noteCacheRepository: NoteCacheRepository,
        noteNetworkRepository: NoteNetworkRepository
    ): NoteSyncInteractors {
        return NoteSyncInteractors(
            SyncNotes(noteCacheRepository, noteNetworkRepository),
            SyncDeletedNotes(noteCacheRepository, noteNetworkRepository)
        )
    }
}