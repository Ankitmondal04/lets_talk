package com.example.letstalk.app.data.remote.viewModel

interface HomeViewModel {
    fun loadRooms()

    fun createRoom(roomLabel: String)

    fun leaveRoom(roomLabel: String)
}