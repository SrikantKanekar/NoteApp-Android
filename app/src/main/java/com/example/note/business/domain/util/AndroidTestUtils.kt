package com.example.note.business.domain.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidTestUtils
@Inject
constructor(
    private val isTest: Boolean
){
    fun isTest() = isTest

}