package com.example.server.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

@Serializable
data class Room(
    val label: String,  //All room name will be unique
    val ownerId: String,
    @SerialName("_id")
    val id: String = ObjectId().toString()
)

class RoomAlreadyExistException: Exception("Room Already Exists")

class RoomDoesNotExistException: Exception("Room Doesn't Exists")