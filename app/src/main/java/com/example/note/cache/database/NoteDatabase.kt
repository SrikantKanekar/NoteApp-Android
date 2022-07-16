package com.example.note.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.cache.database.dao.NoteDao
import com.example.note.cache.database.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME: String = "note_database"
    }
}