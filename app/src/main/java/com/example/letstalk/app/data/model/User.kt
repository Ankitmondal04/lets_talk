package com.example.letstalk.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String,
    val hashedPasscode: String,
    val roomId: List<String>,
    @SerialName("_id")
    val id: String
)
