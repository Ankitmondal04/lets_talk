package com.example.server.data

import com.example.server.model.Message
import com.example.server.model.Room
import com.example.server.model.User

interface DataSource {
    suspend fun getAllMessagesByRoom(roomId: String): List<Message>

    suspend fun insertMessage(message: Message)

    suspend fun getAllMemberOfRoom(roomId: String): List<User>

    suspend fun insertUser(user: User)

    suspend fun getUserByUserName(userName: String): User?

    suspend fun userExistsByUserName(userName: String): Boolean

    suspend fun getRoomIdByLabel(roomLabel: String): String

    suspend fun insertRoom(room: Room)

    suspend fun addRoomToUser(userName: String, roomId: String)

    suspend fun leaveRoom(roomId: String, userName: String)

    suspend fun deleteRoom(roomId: String)

    suspend fun roomExists(roomId: String): Boolean

    suspend fun roomExistsByLabel(roomLabel: String): Boolean



}