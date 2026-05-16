package com.example.server.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

@Serializable
data class Message(
    val text: String,
    val timeStamp: Long,
    val userName: String,
    val roomId: String,
    @SerialName("_id")
    val id: String = ObjectId().toString()
)

class NoParameterGivenException: Exception("Wrong value or parameter")