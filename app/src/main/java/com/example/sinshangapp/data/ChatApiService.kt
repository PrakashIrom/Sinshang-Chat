package com.example.sinshangapp.data

import com.example.sinshangapp.data.model.Message

interface ChatApiService {
    suspend fun listenToMessages(onMessageReceived: (Message)->Unit):Unit?

    suspend fun sendMessage(message: Message)

    companion object{
        const val BASE_URL = "ws://192.168.0.104:8080"
    }

    sealed class Endpoints(val url: String){
        data object Chat: Endpoints("$BASE_URL/messages")
    }
}