package com.example.note.cache.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_table")
data class LabelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)