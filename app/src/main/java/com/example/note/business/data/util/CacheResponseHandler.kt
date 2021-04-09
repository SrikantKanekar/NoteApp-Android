package com.example.note.business.data.util

import com.example.note.business.domain.state.*
import com.example.note.business.domain.util.printLogD

abstract class CacheResponseHandler <ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
){
    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is CacheResult.GenericError -> {
                printLogD(
                    className = "CacheResponseHandler",
                    message = "----------Cache Error-----------\n" +
                            "Error Message : ${response.errorMessage}"
                )
                DataState.error(
                    response = Response(
                        message = "${stateEvent?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        uiType = UiType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    printLogD(
                        className = "CacheResponseHandler",
                        message = "----------Cache Error-----------\n" +
                                "Error : Cache data is null"
                    )
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: $CACHE_DATA_NULL.",
                            uiType = UiType.Dialog,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?

    companion object{
        const val CACHE_DATA_NULL = "Cache data is null"
    }
}