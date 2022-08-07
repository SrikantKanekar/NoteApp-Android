package com.example.note.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.note.cache.database.dao.LabelDao
import com.example.note.cache.database.dao.NoteDao
import com.example.note.cache.database.entity.LabelEntity
import com.example.note.cache.database.entity.NoteEntity

@Database(entities = [NoteEntity::class, LabelEntity::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun labelDao(): LabelDao

    companion object {
        const val DATABASE_NAME: String = "note_database"
    }
}