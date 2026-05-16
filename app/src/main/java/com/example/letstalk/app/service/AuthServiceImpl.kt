package com.example.letstalk.app.service

import com.example.letstalk.app.data.model.AuthRequest
import com.example.letstalk.app.data.model.AuthResponse
import com.example.letstalk.app.data.remote.services.AuthService
import com.example.letstalk.app.util.Constants
import com.example.letstalk.app.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthServiceImpl(
    private val client: HttpClient
): AuthService {
    override suspend fun register(request: AuthRequest): Resource<AuthResponse> {
        return try {
            val response = client.get(Constants.Endpoints.REGISTER) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<AuthResponse>()
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error(e.message?: "Unknown Error")
        }
    }

    override suspend fun login(request: AuthRequest): Resource<AuthResponse> {
        return try {
            val response =  client.get(Constants.Endpoints.LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<AuthResponse>()
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error(e.message?: "Unknown Error")
        }
    }
}