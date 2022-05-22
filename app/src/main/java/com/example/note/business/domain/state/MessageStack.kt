package com.example.note.business.domain.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.note.business.domain.util.printResponse

class MessageStack : ArrayList<StateMessage>() {

    val stateMessage: MutableState<StateMessage?> = mutableStateOf(null)

    private fun setStateMessage(message: StateMessage?) {
        stateMessage.value = message
    }

    override fun add(element: StateMessage): Boolean {
        if (this.contains(element)) { // prevent duplicate errors added to stack
            return false
        }
        val transaction = super.add(element)
        printResponse(element.response)
        if (this.size == 1) {
            setStateMessage(message = element)
        }
        return transaction
    }

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        for (element in elements) {
            add(element)
        }
        // always return true. We don't care about result
        return true
    }

    override fun removeAt(index: Int): StateMessage {
        try {
            if (this.isNotEmpty()) {
                val transaction = super.removeAt(index)
                if (this.size > 0) {
                    setStateMessage(message = this[0])
                } else {
                    setStateMessage(null)
                }
                return transaction
            }
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            setStateMessage(null)
        }
        return StateMessage(
            Response(
                message = "MessageStack is already empty",
                uiType = UiType.None,
                messageType = MessageType.Info
            )
        )
    }
}