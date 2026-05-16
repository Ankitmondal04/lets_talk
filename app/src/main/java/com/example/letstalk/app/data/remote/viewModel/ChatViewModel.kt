package com.example.letstalk.app.data.remote.viewModel

import com.example.letstalk.app.data.remote.services.RoomService

interface ChatViewModel {

    fun onStart(userName: String, roomLabel: String)

    fun sendMessage()

    fun onMessageChange(value: String)

    fun disconnect()
}