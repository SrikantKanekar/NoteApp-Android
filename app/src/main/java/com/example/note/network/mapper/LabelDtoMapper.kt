package com.example.note.network.mapper

import com.example.note.model.Label
import com.example.note.network.dto.LabelDto
import com.example.note.util.Mapper
import javax.inject.Inject

class LabelDtoMapper @Inject constructor() : Mapper<LabelDto, Label> {

    override fun toModel(obj: LabelDto): Label {
        return Label(
            id = obj.id,
            name = obj.name,
            created_at = obj.created_at,
            updated_at = obj.updated_at
        )
    }

    override fun fromModel(model: Label): LabelDto {
        return LabelDto(
            id = model.id,
            name = model.name,
            created_at = model.created_at,
            updated_at = model.updated_at
        )
    }
}
