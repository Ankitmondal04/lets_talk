package com.example.letstalk.app.service

import com.example.letstalk.app.data.remote.services.RoomService
import com.example.letstalk.app.util.Constants
import com.example.letstalk.app.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.client.request.headers

class RoomServiceImpl(
    private val client: HttpClient
): RoomService {
    override suspend fun createRoom(roomLabel: String, token: String): Resource<String> {
        return try {
            val response = client.post("${Constants.Endpoints.CREATE_ROOM}?roomLabel=$roomLabel") {
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body<String>()
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error(e.message?:"Unknown Error")
        }
    }

    override suspend fun getAllMembersOfRoom(
        roomLabel: String,
        token: String
    ): Resource<List<String>> {
        return try {
            val members = client.get("${Constants.Endpoints.ALL_MEMBERS}?roomLabel=$roomLabel"){
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body<List<String>>()
            Resource.Success(members)
        }catch (e: Exception) {
            Resource.Error(e.message?:"Unknown Error")
        }
    }

    override suspend fun getAllRoomsOfUser(token: String): Resource<List<String>> {
        return try {
            val rooms = client.get("$Constants.Endpoints.ALL_ROOMS"){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body<List<String>>()
            Resource.Success(rooms)
        }catch (e: Exception) {
            Resource.Error(e.message?: "Unknown Error")
        }
    }

    override suspend fun leaveRoom(roomLabel: String, token: String): Resource<String> {
        return try {
            val response = client.delete("${Constants.Endpoints.LEAVE_ROOM}?roomLabel=$roomLabel") {
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body<String>()
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error(e.message?: "Unknown Error")
        }
    }

    override suspend fun deleteRoom(roomLabel: String, token: String): Resource<String> {
        return try {
            val response = client.delete("${Constants.Endpoints.DELETE_ROOM}?roomLabel=$roomLabel"){
                headers{
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }.body<String>()
            Resource.Success(response)
        }catch (e: Exception) {
            Resource.Error(e.message?:"Unknown Error")
        }
    }

}