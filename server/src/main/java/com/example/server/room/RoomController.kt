package com.example.server.room

import io.ktor.websocket.WebSocketSession

interface RoomController {
    suspend fun onJoin(userName: String, roomLabel: String, socketId: WebSocketSession)

    suspend fun tryDisconnect(userName: String, roomLabel: String)

    suspend fun createRoom(roomLabel: String, owner: String)

    suspend fun sendMessage(message: String, userName: String, roomLabel: String)
}