package com.example.note.framework.datasource.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "body")
    var body: String,

    @ColumnInfo(name = "updated_at")
    var updated_at: String,

    @ColumnInfo(name = "created_at")
    var created_at: String

) {
    companion object {

        fun nullTitleError(): String {
            return "You must enter a title."
        }

        fun nullIdError(): String {
            return "NoteEntity object has a null id. This should not be possible. Check local database."
        }
    }
}