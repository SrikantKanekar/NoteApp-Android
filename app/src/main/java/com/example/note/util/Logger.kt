package com.example.note.util

import android.util.Log
import com.example.note.network.response.SimpleResponse

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

fun printServerResponse(name: String, response: SimpleResponse) {
    printLogD(
        className = "Server Message--------> ",
        message = "Request: $name, " +
                "Successful: ${response.successful}, " +
                "Message: ${response.message}"
    )
}