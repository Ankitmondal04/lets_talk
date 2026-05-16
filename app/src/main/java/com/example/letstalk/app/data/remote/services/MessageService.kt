package com.example.letstalk.app.data.remote.services

import com.example.letstalk.app.data.model.Message
import com.example.letstalk.app.util.Resource

interface MessageService{

    suspend fun getAllMessagesOfRoom(roomLabel: String, token: String): Resource<List<Message>>

    //suspend fun sendMessageToRoom(message: Message)

}