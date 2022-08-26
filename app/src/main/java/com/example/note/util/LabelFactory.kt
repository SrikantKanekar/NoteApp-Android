package com.example.note.util

import com.example.note.model.Label
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelFactory @Inject constructor(
    private val dateUtil: DateUtil
) {

    fun createLabel(
        id: String? = null,
        name: String,
        created_at: String? = null,
        updated_at: String? = null
    ): Label {
        return Label(
            id = id ?: UUID.randomUUID().toString(),
            name = name,
            created_at = created_at ?: dateUtil.getCurrentTimestamp(),
            updated_at = updated_at ?: dateUtil.getCurrentTimestamp()
        )
    }
}
