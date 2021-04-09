package com.example.note.framework.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class ], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{
        val DATABASE_NAME: String = "note_db"
    }
}