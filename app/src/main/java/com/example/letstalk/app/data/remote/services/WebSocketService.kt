package com.example.letstalk.app.data.remote.services

import com.example.letstalk.app.data.model.Message
import com.example.letstalk.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface WebSocketService {

    suspend fun initSession(userName: String, roomLabel: String, token: String): Resource<Unit>

    //suspend fun connectToRoom(userName: String, roomLabel: String)

    suspend fun sendMessage(message: String)

    suspend fun observeMessage(): Flow<Message>

    //suspend fun disconnectToRoom(userName: String, roomLabel: String)

    suspend fun closeSession()
}