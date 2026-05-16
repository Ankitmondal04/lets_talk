package com.example.letstalk.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: String,
    val userName: String,
    val timeStamp: Long,
    val roomId: String,
    @SerialName("_id")
    val id: String
)
