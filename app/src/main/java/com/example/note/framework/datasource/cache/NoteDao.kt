package com.example.note.framework.datasource.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

const val NOTE_ORDER_ASC: String = ""
const val NOTE_ORDER_DESC: String = "-"
const val NOTE_FILTER_TITLE = "title"
const val NOTE_FILTER_DATE_CREATED = "created_at"

const val ORDER_BY_ASC_DATE_UPDATED = NOTE_ORDER_ASC + NOTE_FILTER_DATE_CREATED
const val ORDER_BY_DESC_DATE_UPDATED = NOTE_ORDER_DESC + NOTE_FILTER_DATE_CREATED
const val ORDER_BY_ASC_TITLE = NOTE_ORDER_ASC + NOTE_FILTER_TITLE
const val ORDER_BY_DESC_TITLE = NOTE_ORDER_DESC + NOTE_FILTER_TITLE

const val NOTE_PAGINATION_PAGE_SIZE = 30

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotes(notes: List<NoteEntity>): LongArray

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun searchNoteById(id: String): NoteEntity?

    @Query("DELETE FROM note_table WHERE id IN (:ids)")
    suspend fun deleteNotes(ids: List<String>): Int

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_table")
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("""
        UPDATE note_table 
        SET 
        title = :title, 
        body = :body,
        updated_at = :updated_at
        WHERE id = :primaryKey
        """)
    suspend fun updateNote(
        primaryKey: String,
        title: String,
        body: String?,
        updated_at: String
    ): Int

    @Query("DELETE FROM note_table WHERE id = :primaryKey")
    suspend fun deleteNote(primaryKey: String): Int

    @Query("SELECT * FROM note_table")
    suspend fun searchNotes(): List<NoteEntity>

    @Query("""
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY updated_at DESC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("""
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY updated_at ASC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("""
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY title DESC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>

    @Query("""
        SELECT * FROM note_table 
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        ORDER BY title ASC LIMIT (:page * :pageSize)
        """)
    suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int = NOTE_PAGINATION_PAGE_SIZE
    ): List<NoteEntity>


    @Query("SELECT COUNT(*) FROM note_table")
    suspend fun getNumNotes(): Int
}


suspend fun NoteDao.returnOrderedQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): List<NoteEntity> {

    when{

        filterAndOrder.contains(ORDER_BY_DESC_DATE_UPDATED) ->{
            return searchNotesOrderByDateDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED) ->{
            return searchNotesOrderByDateASC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_DESC_TITLE) ->{
            return searchNotesOrderByTitleDESC(
                query = query,
                page = page)
        }

        filterAndOrder.contains(ORDER_BY_ASC_TITLE) ->{
            return searchNotesOrderByTitleASC(
                query = query,
                page = page)
        }
        else ->
            return searchNotesOrderByDateDESC(
                query = query,
                page = page
            )
    }
}
