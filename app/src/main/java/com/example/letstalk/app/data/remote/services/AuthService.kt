package com.example.letstalk.app.data.remote.services

import com.example.letstalk.app.data.model.AuthRequest
import com.example.letstalk.app.data.model.AuthResponse
import com.example.letstalk.app.util.Resource

interface AuthService {
    suspend fun register(request: AuthRequest): Resource<AuthResponse>

    suspend fun login(request: AuthRequest): Resource<AuthResponse>
}