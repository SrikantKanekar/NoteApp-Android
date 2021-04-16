package com.example.note.business.domain.util

import android.util.Log
import com.example.note.business.domain.state.Response
import com.example.note.business.domain.util.Constant.DEBUG
import com.example.note.business.domain.util.Constant.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

fun printResponse(response: Response){
    printLogD(
        className = "<--------------Message------------>",
        message = "\n" +
                "Message     : ${response.message} \n" +
                "UiType      : ${response.uiType.javaClass.simpleName} \n" +
                "MessageType : ${response.messageType.javaClass.simpleName}"
    )
}

object Constant {
    const val TAG = "APPDEBUG"
    const val DEBUG = true
}