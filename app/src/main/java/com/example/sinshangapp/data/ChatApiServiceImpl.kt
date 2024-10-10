package com.example.sinshangapp.data

import com.example.sinshangapp.data.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatApiServiceImpl(
    private val client: HttpClient
): ChatApiService {

    private var webSocketSession: WebSocketSession? = null
    private val sessionMutex = Mutex()

    private suspend fun createSession(){
        sessionMutex.withLock {
            if (webSocketSession == null) {
                webSocketSession = client.webSocketSession(ChatApiService.Endpoints.Chat.url)
            }
        }
    }

    /*The critical point here is that listenToMessages does not need to be "recalled" when a new message comes in.
    The loop (for (frame in incoming)) keeps the WebSocket session "alive" and continuously listens for new frames
    (i.e., messages) from the server.*/
    override suspend fun listenToMessages(onMessageReceived: (Message) -> Unit ): Unit? {
        return try{
            createSession()
            webSocketSession?.let{
                    session ->
                for(frame in session.incoming){
                    when(frame){
                        is Frame.Text -> {
                            val jsonMessage = frame.readText()
                            val message = Json.decodeFromString<Message>(jsonMessage)
                            onMessageReceived(message)
                        }
                        else -> Unit
                    }
                }
            }
        } catch(e : Exception){
            e.printStackTrace()
        }
    }

    override suspend fun sendMessage(message: Message) {
        try {
            createSession()
            val jsonMessage = Json.encodeToString(message)
            webSocketSession?.send(Frame.Text(jsonMessage))
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }

    suspend fun closeWebSocket(){
        webSocketSession?.close()
        webSocketSession = null
    }

}