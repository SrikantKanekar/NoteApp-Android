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
        createdAt: String? = null,
        updatedAt: String? = null
    ): Label {
        return Label(
            id = id ?: UUID.randomUUID().toString(),
            name = name,
            created_at = createdAt ?: dateUtil.getCurrentTimestamp(),
            updated_at = updatedAt ?: dateUtil.getCurrentTimestamp()
        )
    }

    fun createLabels(numLabels: Int): List<Label> {
        val list: ArrayList<Label> = ArrayList()
        for (i in 0 until numLabels) {
            list.add(
                createLabel(
                    name = UUID.randomUUID().toString(),
                )
            )
        }
        return list
    }

    fun createYesterdayLabel(id: String? = null): Label {
        return createLabel(
            id = id,
            name = UUID.randomUUID().toString(),
            createdAt = dateUtil.getYesterdayTimestamp(),
            updatedAt = dateUtil.getYesterdayTimestamp()
        )
    }
}
