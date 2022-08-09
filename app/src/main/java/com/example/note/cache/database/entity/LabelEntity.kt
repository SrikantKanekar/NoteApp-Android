package com.example.note.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_table")
data class LabelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val created_at: String,
    val updated_at: String
)