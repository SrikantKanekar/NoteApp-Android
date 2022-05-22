package com.example.note.business.domain.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtil @Inject constructor() {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)

    fun removeTimeFromDateString(sd: String): String {
        return sd.substring(0, sd.indexOf(" "))
    }

    // dates format looks like this: "2019-07-23 HH:mm:ss"
    fun getCurrentTimestamp(): String {
        return simpleDateFormat.format(Date())
    }
}
