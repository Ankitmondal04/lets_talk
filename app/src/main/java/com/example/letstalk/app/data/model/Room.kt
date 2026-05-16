package com.example.letstalk.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val label: String,
    val ownerId: String,
    @SerialName("_id")
    val roomId: String
)
