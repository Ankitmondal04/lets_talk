package com.example.letstalk.app.service

import com.example.letstalk.app.data.model.Message
import com.example.letstalk.app.data.remote.services.MessageService
import com.example.letstalk.app.util.Constants
import com.example.letstalk.app.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.client.request.headers

class MessageServiceImpl(
    private val client: HttpClient
): MessageService {
    override suspend fun getAllMessagesOfRoom(
        roomLabel: String,
        token: String
    ): Resource<List<Message>> {
        return try {
            val messages = client.get("${Constants.Endpoints.ALL_MESSAGES}?roomLabel=$roomLabel") {
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body<List<Message>>()
            Resource.Success(messages)
        }catch (e: Exception) {
            Resource.Error(e.message?: "Unknown Error")
        }
    }

}