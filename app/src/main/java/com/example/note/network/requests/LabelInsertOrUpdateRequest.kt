package com.example.note.network.requests

import com.example.note.network.dto.LabelDto

data class LabelInsertOrUpdateRequest(
    val labels: List<LabelDto>
)