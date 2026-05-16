package com.example.server.model

import io.ktor.websocket.WebSocketSession
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

@Serializable
data class User(
    val userName: String,
    val password: String,
    val roomsId: List<String>, //Will store all the room names that the user has entered
    @SerialName("_id")
    val id: String = ObjectId().toString()

)

data class UserSession(
    val userName: String,
    val session: WebSocketSession
)

class UserAlreadyExistException: Exception("User Already Exists")

class UserDoesNotExistException: Exception("User Doesn't Exists")