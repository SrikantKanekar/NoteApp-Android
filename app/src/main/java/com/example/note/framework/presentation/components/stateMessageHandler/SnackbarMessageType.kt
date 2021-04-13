package com.example.note.framework.presentation.components.stateMessageHandler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.note.business.domain.state.MessageType
import com.example.note.business.domain.state.Response
import com.example.note.framework.presentation.components.snackbar.ErrorSnackbar
import com.example.note.framework.presentation.components.snackbar.InfoSnackbar
import com.example.note.framework.presentation.components.snackbar.SuccessSnackbar
import com.example.note.framework.presentation.theme.snackbarController
import kotlinx.coroutines.launch

@Composable
fun SnackbarMessageType(
    response: Response,
    scaffoldState: ScaffoldState,
    removeStateMessage: () -> Unit
) {
    response.message?.let {

        Box(modifier = Modifier.fillMaxSize()) {

            snackbarController.getScope().launch {
                snackbarController.showSnackbar(
                    scaffoldState = scaffoldState,
                    message = response.message,
                    actionLabel = "Ok",
                    removeStateMessage = removeStateMessage
                )
            }

            when(response.messageType){

                MessageType.Success -> {
                    SuccessSnackbar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        snackbarHostState = scaffoldState.snackbarHostState,
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            removeStateMessage()
                        }
                    )
                }

                MessageType.Error -> {
                    ErrorSnackbar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        snackbarHostState = scaffoldState.snackbarHostState,
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            removeStateMessage()
                        }
                    )
                }

                MessageType.Info -> {
                    InfoSnackbar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        snackbarHostState = scaffoldState.snackbarHostState,
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