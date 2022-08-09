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

    @Insert
    suspend fun insertLabels(labels: List<LabelEntity>): LongArray

    @Update
    suspend fun updateLabel(label: LabelEntity)

    @Update
    suspend fun updateLabels(labels: List<LabelEntity>): Int

    @Query("DELETE FROM label_table WHERE id = :id")
    suspend fun deleteLabel(id: String)

    @Query("DELETE FROM label_table WHERE id IN (:ids)")
    suspend fun deleteLabels(ids: List<String>): Int

    @Query("SELECT * FROM label_table WHERE id = :id")
    suspend fun getLabel(id: String): LabelEntity?

    @Query("SELECT * FROM label_table")
    suspend fun getAllLabels(): List<LabelEntity>

    @Query(
        """
        SELECT * FROM label_table 
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name DESC
        """
    )
    fun searchLabels(query: String): Flow<List<LabelEntity>>
}