package com.example.letstalk.app.data.remote.services

import com.example.letstalk.app.util.Resource


interface RoomService {

    suspend fun getAllRoomsOfUser(token: String): Resource<List<String>>

    suspend fun getAllMembersOfRoom(roomLabel: String, token: String): Resource<List<String>>

    suspend fun createRoom(roomLabel: String, token: String): Resource<String>

    //suspend fun searchRoomByLabel(roomLabel: String)

    suspend fun leaveRoom(roomLabel: String, token: String): Resource<String>

    suspend fun deleteRoom(roomLabel: String, token: String): Resource<String>

}