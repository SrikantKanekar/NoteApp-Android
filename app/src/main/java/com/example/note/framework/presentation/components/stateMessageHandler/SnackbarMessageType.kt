package com.example.note.framework.presentation.components.stateMessageHandler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.note.business.domain.state.MessageType
import com.example.note.business.domain.state.Response
import com.example.note.framework.presentation.components.snackbar.ErrorSnackBar
import com.example.note.framework.presentation.components.snackbar.InfoSnackBar
import com.example.note.framework.presentation.components.snackbar.SuccessSnackBar
import com.example.note.framework.presentation.theme.snackBarController
import kotlinx.coroutines.launch

@Composable
fun SnackBarMessageType(
    response: Response,
    scaffoldState: ScaffoldState,
    removeStateMessage: () -> Unit
) {
    response.message?.let { message ->

        Box(modifier = Modifier.fillMaxSize()) {

            LaunchedEffect(message) {
                snackBarController.getScope().launch {
                    snackBarController.showSnackBar(
                        scaffoldState = scaffoldState,
                        message = message,
                        actionLabel = "Ok",
                        removeStateMessage = removeStateMessage
                    )
                }
            }

            when(response.messageType){

                MessageType.Success -> {
                    SuccessSnackBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        snackBarHostState = scaffoldState.snackbarHostState,
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            removeStateMessage()
                        }
                    )
                }

                MessageType.Error -> {
                    ErrorSnackBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        snackBarHostState = scaffoldState.snackbarHostState,
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            removeStateMessage()
                        }
                    )
                }

                MessageType.Info -> {
                    InfoSnackBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        snackBarHostState = scaffoldState.snackbarHostState,
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            removeStateMessage()
                        }
                    )
                }
            }
        }
    }
}