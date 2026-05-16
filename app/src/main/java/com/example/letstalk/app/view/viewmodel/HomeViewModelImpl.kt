package com.example.letstalk.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letstalk.app.data.local.TokenManager
import com.example.letstalk.app.data.remote.services.RoomService
import com.example.letstalk.app.data.remote.viewModel.HomeViewModel
import com.example.letstalk.app.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModelImpl(
    private val roomService: RoomService,
    private val tokenManager: TokenManager
): HomeViewModel, ViewModel() {
    private val _rooms = MutableStateFlow<Resource<List<String>>>(Resource.Loading())
    val rooms: StateFlow<Resource<List<String>>> = _rooms

    private val _createRoomState = MutableStateFlow<Resource<String>?>(null)
    val createRoomState: StateFlow<Resource<String>?> = _createRoomState

    private val _newRoomLabel = MutableStateFlow("")
    val newRoomLabel: StateFlow<String> = _newRoomLabel

    init {
        loadRooms()
    }

    fun onRoomLabelChange(value: String) {
        _newRoomLabel.value = value
    }

    override fun loadRooms() {
        viewModelScope.launch {
            _rooms.value = Resource.Loading()

            val token = tokenManager.getToken()
            if(token == null) {
                _rooms.value = Resource.Error("Not authenticated")
                return@launch
            }

            _rooms.value = roomService.getAllRoomsOfUser(token)
        }
    }

    override fun createRoom(roomLabel: String) {
        viewModelScope.launch {
            if (_newRoomLabel.value.isBlank()) {
                _createRoomState.value = Resource.Error("Room cannot be emty")
                return@launch
            }

            _createRoomState.value = Resource.Loading()

            val token = tokenManager.getToken()
            if(token == null) {
                _rooms.value = Resource.Error("Not authenticated")
                return@launch
            }

            val result = roomService.createRoom(roomLabel, token)
            _createRoomState.value = result

            if(result is Resource.Success) {
                _newRoomLabel.value = ""
                loadRooms()
            }
        }
    }

    override fun leaveRoom(roomLabel: String) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if(token == null) {
                _rooms.value = Resource.Error("Not Authenticated")
                return@launch
            }

            val result = roomService.leaveRoom(roomLabel, token)
            if(result is Resource.Success){
                loadRooms()
            }
        }
    }
}