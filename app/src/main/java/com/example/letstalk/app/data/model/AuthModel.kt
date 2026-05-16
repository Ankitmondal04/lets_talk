package com.example.letstalk.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val userName: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val message: String
)
