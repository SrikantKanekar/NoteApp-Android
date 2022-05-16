package com.example.note.framework.datasource.data

import android.app.Application
import android.content.res.AssetManager
import com.example.note.business.domain.model.Note
import com.example.note.business.domain.model.NoteFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDataFactory
@Inject
constructor(
    private val application: Application,
    private val noteFactory: NoteFactory
) {

    fun produceListOfNotes(): List<Note> {
        return Gson()
            .fromJson(
                readJSONFromAsset("note_list.json"),
                object : TypeToken<List<Note>>() {}.type
            )
    }

    private fun readJSONFromAsset(fileName: String): String? {
        return try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    fun createSingleNote(
        id: String? = null,
        title: String? = null,
        body: String? = null
    ) = noteFactory.createSingleNote(id, title, body)

    fun createNoteList(num: Int) = noteFactory.createNoteList(num)
}