package com.example.note.business.domain.state

data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val uiType: UiType,
    val messageType: MessageType
)

sealed class UiType {
    object Dialog : UiType()

    object SnackBar : UiType()

    object AreYouSureDialog : UiType()

    object None : UiType()
}

sealed class MessageType {
    object Success : MessageType()

    object Error : MessageType()

    object Info : MessageType()
}