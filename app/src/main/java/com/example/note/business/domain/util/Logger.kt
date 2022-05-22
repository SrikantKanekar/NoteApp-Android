package com.example.note.business.domain.util

import android.util.Log
import com.example.note.business.domain.state.Response
import com.example.note.business.domain.util.Constant.DEBUG
import com.example.note.business.domain.util.Constant.TAG
import com.example.note.framework.datasource.network.response.SimpleResponse

var isUnitTest = false

fun printLogD(className: String?, message: String) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    } else if (DEBUG && isUnitTest) {
        println("$className: $message")
    }
}

fun printResponse(response: Response) {
    printLogD(
        className = "State Message--------> ",
        message = "Message: ${response.message}, " +
                "UiType: ${response.uiType.javaClass.simpleName}, " +
                "MessageType: ${response.messageType.javaClass.simpleName}"
    )
}

fun printServerResponse(name: String, response: SimpleResponse) {
    printLogD(
        className = "Server Message--------> ",
        message = "Request: $name, " +
                "Successful: ${response.successful}, " +
                "Message: ${response.message}"
    )
}

object Constant {
    const val TAG = "APP_DEBUG"
    const val DEBUG = true
}