package com.example.note.util

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtil @Inject constructor() {
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)

    fun getCurrentTimestamp(): String {
        return simpleDateFormat.format(Date())
    }

    fun getYesterdayTimestamp(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return simpleDateFormat.format(calendar.time)
    }

    fun getTimestamp(date: Date): String {
        return simpleDateFormat.format(date)
    }

    fun removeTimeFromDateString(sd: String): String {
        return sd.substring(0, sd.indexOf(" "))
    }
}
