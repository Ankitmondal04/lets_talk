package com.example.letstalk.app.util

object Constants {
    const val BASE_URL = "http://10.0.2.2:8080"
    const val BASE_WS_URL = "ws://10.0.2.2:8080"

    object Endpoints {
        const val REGISTER = "$BASE_URL/auth/register"
        const val LOGIN = "$BASE_URL/auth/login"
        const val CREATE_ROOM = "$BASE_URL/room/create"
        const val LEAVE_ROOM = "$BASE_URL/room/leave"
        const val DELETE_ROOM = "$BASE_URL/room/delete"
        const val ALL_MEMBERS = "$BASE_URL/room/allMembers"
        const val ALL_MESSAGES = "$BASE_URL/allMessages"
        const val ALL_ROOMS = "$BASE_URL/room/all"
        const val CHAT_SOCKET = "$BASE_WS_URL/chat"
    }
}