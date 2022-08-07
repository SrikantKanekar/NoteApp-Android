package com.example.note.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.note.cache.database.entity.LabelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {

    @Insert
    suspend fun insertLabel(label: LabelEntity)

    @Update
    suspend fun updateLabel(label: LabelEntity)

    @Query("DELETE FROM label_table WHERE id = :id")
    suspend fun deleteLabel(id: String)

    @Query("SELECT * FROM label_table WHERE id = :id")
    suspend fun getLabel(id: String): LabelEntity?

    @Query("SELECT * FROM label_table")
    fun getAllLabels(): Flow<List<LabelEntity>>
}