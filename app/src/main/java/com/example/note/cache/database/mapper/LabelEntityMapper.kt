package com.example.note.cache.database.mapper

import com.example.note.cache.database.entity.LabelEntity
import com.example.note.model.Label
import com.example.note.util.Mapper
import javax.inject.Inject

class LabelEntityMapper @Inject constructor() : Mapper<LabelEntity, Label> {

    override fun toModel(obj: LabelEntity): Label {
        return Label(
            id = obj.id,
            name = obj.name,
        )
    }

    override fun fromModel(model: Label): LabelEntity {
        return LabelEntity(
            id = model.id,
            name = model.name,
        )
    }
}
