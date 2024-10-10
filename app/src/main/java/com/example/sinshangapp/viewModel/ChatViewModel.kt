package com.example.sinshangapp.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sinshangapp.data.ChatApiService
import com.example.sinshangapp.data.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed interface SendState{
    data object Loading: SendState
    data object Error: SendState
    data object Success: SendState
}

class ChatViewModel(private val chatApiService: ChatApiService): ViewModel() {

    private var chatState = MutableStateFlow<SendState>(SendState.Loading)
    val _chatState = chatState

    private var receiveState = mutableStateListOf<Message>()
    val _receiveState = receiveState

    suspend fun sendMessage(message: Message){
        chatState.value = SendState.Loading
        try{
            chatApiService.sendMessage(message)
            receiveState.add(message)
            chatState.value = SendState.Success
        }
        catch(e: Exception){
            chatState.value = SendState.Error
        }
    }

    init
    {
        viewModelScope.launch{
            chatApiService.listenToMessages { message ->
                receiveState.add(message)
            }
        }
    }

}