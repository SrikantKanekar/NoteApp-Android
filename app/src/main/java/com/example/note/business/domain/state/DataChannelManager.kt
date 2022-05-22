package com.example.note.business.domain.state

import com.example.note.business.domain.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

abstract class DataChannelManager<ViewState> {

    private val stateEventManager: StateEventManager = StateEventManager()
    private var channelScope: CoroutineScope? = null
    val shouldDisplayProgressBar = stateEventManager.shouldDisplayProgressbar
    val messageStack = MessageStack()

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>?>
    ) {
        val canExecute = canExecuteNewStateEvent(stateEvent)
//        printLogD(
//            "DataChannelManager",
//            "Can Execute New StateEvent : $canExecute"
//        )
        if (canExecute) {
            addStateEvent(stateEvent)
            jobFunction
                .onEach { dataState ->
                    dataState?.let { dState ->
                        withContext(Main) {
                            dState.data?.let { data ->
                                handleNewData(data)
                            }
                            dState.stateMessage?.let { stateMessage ->
                                addNewStateMessage(stateMessage)
                            }
                            dState.stateEvent?.let { stateEvent ->
                                removeStateEvent(stateEvent)
                            }
                        }
                    }
                }
                .launchIn(getChannelScope())
        }
    }

    private fun canExecuteNewStateEvent(stateEvent: StateEvent): Boolean {
        // If a job is already active, do not allow duplication
        if (isStateEventActive(stateEvent)) {
            return false
        }
        return true
    }

    abstract fun handleNewData(data: ViewState)


    // StateEventManager Functions
    private fun isStateEventActive(stateEvent: StateEvent) =
        stateEventManager.isStateEventActive(stateEvent)

    private fun addStateEvent(stateEvent: StateEvent) = stateEventManager.addStateEvent(stateEvent)

    private fun removeStateEvent(stateEvent: StateEvent?) =
        stateEventManager.removeStateEvent(stateEvent)

    fun getActiveStateEvents() = stateEventManager.getActiveStateEvents()

    fun clearActiveStateEvents() = stateEventManager.clearActiveStateEvents()


    // MessageStack Functions
    fun addNewStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun removeStateMessage(index: Int = 0) {
        messageStack.removeAt(index)
    }

    private fun isMessageStackEmpty(): Boolean {
        return messageStack.isEmpty()
    }

    fun clearAllStateMessages() {
        messageStack.clear()
//        printLogD("DataChannelManager", "Cleared All State Messages")
    }

    fun printAllStateMessages() {
        for (message in messageStack) {
            printLogD("DataChannelManager", "${message.response.message}")
        }
    }


    // Channel Scope Functions
    private fun getChannelScope(): CoroutineScope {
        return channelScope ?: setupNewChannelScope(CoroutineScope(IO))
    }

    private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope {
        channelScope = coroutineScope
//        printLogD("DataChannelManager", "Setting up New IO ChannelScope")
        return channelScope as CoroutineScope
    }

    private fun clearChannelScope() {
        if (channelScope != null) {
            if (channelScope?.isActive == true) {
                channelScope?.cancel()
            }
            channelScope = null
        }
//        printLogD("DataChannelManager", "Cleared active ChannelScope")
    }

    fun cancelActiveJobs() {
        clearChannelScope()
        clearActiveStateEvents()
        printLogD("DataChannelManager", "<<<<<------------------------------------>>>>>")
    }
}