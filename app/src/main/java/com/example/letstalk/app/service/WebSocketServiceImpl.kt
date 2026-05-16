package com.example.letstalk.app.service

import com.example.letstalk.app.data.model.Message
import com.example.letstalk.app.data.remote.services.WebSocketService
import com.example.letstalk.app.util.Constants
import com.example.letstalk.app.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class WebSocketServiceImpl(
    private val client: HttpClient
): WebSocketService {
    private var socket: WebSocketSession? = null

    override suspend fun initSession(
        userName: String,
        roomLabel: String,
        token: String
    ): Resource<Unit> {
        return try {
            socket = client.webSocketSession{
                url("${Constants.Endpoints.CHAT_SOCKET}?userName=$userName&roomLabel=$roomLabel")
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            }else {
                Resource.Error("Couldn't connect to the room")
            }
        }catch (e: Exception) {
            Resource.Error(e.message?:"Unknown Error")
        }
    }

    override suspend fun sendMessage(message: String) {
        socket?.send(Frame.Text(message))
    }

    override suspend fun observeMessage(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as Frame.Text).readText()
                    Json.decodeFromString<Message>(json)
                }?: emptyFlow()
        }catch (e: Exception) {
            print(e.message)
            emptyFlow()

        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}