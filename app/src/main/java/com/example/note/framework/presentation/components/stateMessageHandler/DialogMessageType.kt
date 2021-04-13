package com.example.note.framework.presentation.components.stateMessageHandler

import androidx.compose.runtime.Composable
import com.example.note.business.domain.state.MessageType
import com.example.note.business.domain.state.Response
import com.example.note.framework.presentation.components.dialog.MyErrorDialog
import com.example.note.framework.presentation.components.dialog.MyInfoDialog
import com.example.note.framework.presentation.components.dialog.MySuccessDialog

@Composable
fun DialogMessageType(
    response: Response,
    removeStateMessage: () -> Unit
) {
    response.message?.let { message ->
        when(response.messageType){

            MessageType.Success -> {
                MySuccessDialog(
                    message = message,
                    removeStateMessage = removeStateMessage
                )
            }

            MessageType.Error -> {
                MyErrorDialog(
                    message = message,
                    removeStateMessage = removeStateMessage
                )
            }

            MessageType.Info -> {
                MyInfoDialog(
                    message = message,
                    removeStateMessage = removeStateMessage
                )
            }
        }
    }
}