package com.example.note.util

interface Mapper<T, Model> {

    fun toModel(obj: T): Model
    fun fromModel(model: Model): T
}