package com.example.note.business.domain.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Singleton
class DateUtil
constructor(
    private val dateFormat: SimpleDateFormat
) {

    fun removeTimeFromDateString(sd: String): String {
        return sd.substring(0, sd.indexOf(" "))
    }

    // dates format looks like this: "2019-07-23 HH:mm:ss"
    fun getCurrentTimestamp(): String {
        return dateFormat.format(Date())
    }
}
