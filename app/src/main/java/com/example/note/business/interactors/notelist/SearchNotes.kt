package com.example.note.business.interactors.notelist

import com.example.note.business.data.cache.NoteCacheRepository
import com.example.note.framework.datasource.cache.NOTE_FILTER_DATE_CREATED
import com.example.note.framework.datasource.cache.NOTE_ORDER_DESC

class SearchNotes(
    private val noteCacheRepository: NoteCacheRepository
) {

    fun execute(
        query: String? = "",
        filterAndOrder: String = NOTE_ORDER_DESC + NOTE_FILTER_DATE_CREATED,
        page: Int = 1
    ) = noteCacheRepository.searchNotes(
        query = query ?: "",
        filterAndOrder = filterAndOrder,
        page = page
    )
}