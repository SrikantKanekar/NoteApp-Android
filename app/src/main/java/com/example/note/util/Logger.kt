package com.example.note.util

import android.util.Log

var isUnitTest = false
var isDebug = true

fun printLogD(className: String?, message: String) {
    if (isDebug) {
        when (isUnitTest) {
            true -> println("$className: $message")
            false -> Log.d(TAG, "$className: $message")
        }
    }
}