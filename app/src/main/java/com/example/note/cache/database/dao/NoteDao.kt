package com.example.note.cache.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.note.cache.database.entity.NoteEntity
import com.example.note.util.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNotes(notes: List<NoteEntity>): LongArray

    @Update
    suspend fun updateNotes(notes: List<NoteEntity>): Int

    @Query("DELETE FROM note_table WHERE id IN (:ids)")
    suspend fun deleteNotes(ids: List<String>): Int

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNote(id: String): NoteEntity?

    @Query("SELECT * FROM note_table")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query(
        """
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY createdAt DESC LIMIT (:page * :pageSize)
        """
    )
    fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): Flow<List<NoteEntity>>

    @Query(
        """
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY createdAt ASC LIMIT (:page * :pageSize)
        """
    )
    fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): Flow<List<NoteEntity>>

    @Query(
        """
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY title DESC LIMIT (:page * :pageSize)
        """
    )
    fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): Flow<List<NoteEntity>>


    @Query(
        """
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY title ASC LIMIT (:page * :pageSize)
        """
    )
    fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): Flow<List<NoteEntity>>
}

fun NoteDao.searchNotes(
    query: String,
    filterAndOrder: String,
    page: Int
): Flow<List<NoteEntity>> {

    when {
        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED) -> {
            return searchNotesOrderByDateDESC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED) -> {
            return searchNotesOrderByDateASC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_DESC_TITLE) -> {
            return searchNotesOrderByTitleDESC(
                query = query,
                page = page
            )
        }

        filterAndOrder.contains(ORDER_BY_ASC_TITLE) -> {
            return searchNotesOrderByTitleASC(
                query = query,
                page = page
            )
        }
        else ->
            return searchNotesOrderByDateDESC(
                query = query,
                page = page
            )
    }
}
