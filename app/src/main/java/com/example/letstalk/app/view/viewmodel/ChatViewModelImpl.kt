package com.example.letstalk.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letstalk.app.data.local.TokenManager
import com.example.letstalk.app.data.model.Message
import com.example.letstalk.app.data.remote.services.MessageService
import com.example.letstalk.app.data.remote.services.WebSocketService
import com.example.letstalk.app.data.remote.viewModel.ChatViewModel
import com.example.letstalk.app.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ChatViewModelImpl(
    private val messageService: MessageService,
    private val webSocketService: WebSocketService,
    private val tokenManager: TokenManager
): ChatViewModel, ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _messageText = MutableStateFlow<String>("")
    val messageText: StateFlow<String> = _messageText

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    private suspend fun loadMessages(roomLabel: String, token: String) {
        _isLoading.value = true
        when (val result = messageService.getAllMessagesOfRoom(roomLabel, token)) {
            is Resource.Success -> {
                _messages.value = result.data ?: emptyList()
            }
            is Resource.Error -> {
                _toastEvent.emit(result.message ?: "Failed to load messages")
            }
            is Resource.Loading -> Unit
        }
        _isLoading.value = false
    }

    private suspend fun observeMessage() {
        viewModelScope.launch {
            webSocketService.observeMessage().collect { newMessage ->
                val currentMessage = _messages.value.toMutableList()
                currentMessage.add(newMessage)
                _messages.value = currentMessage
            }
        }
    }

    private suspend fun connectToSocket(userName: String, roomLabel: String, token: String) {
        when(val result = webSocketService.initSession(userName, roomLabel, token)) {
            is Resource.Success -> observeMessage()
            is Resource.Error -> _toastEvent.emit(result.message ?: "Failed to connect")
            is Resource.Loading -> Unit
        }
    }

    override fun onStart(userName: String, roomLabel: String) {
        viewModelScope.launch {
            val token = tokenManager.getToken()
            if(token == null) {
                _toastEvent.emit("Not Authenticated")
                return@launch
            }
            loadMessages(roomLabel, token)
            connectToSocket(userName, roomLabel, token)
        }
    }

    override fun sendMessage() {
        viewModelScope.launch {
            val messageText = _messageText.value.trim()
            if(messageText.isBlank()) return@launch
            webSocketService.sendMessage(messageText)
            _messageText.value = ""
        }
    }

    override fun onMessageChange(value: String) {
        _messageText.value = value
    }

    override fun disconnect() {
        viewModelScope.launch {
            webSocketService.closeSession()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}