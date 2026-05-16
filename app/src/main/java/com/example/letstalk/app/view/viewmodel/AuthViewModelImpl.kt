package com.example.letstalk.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letstalk.app.data.local.TokenManager
import com.example.letstalk.app.data.model.AuthRequest
import com.example.letstalk.app.data.model.AuthResponse
import com.example.letstalk.app.data.remote.services.AuthService
import com.example.letstalk.app.data.remote.viewModel.AuthViewModel
import com.example.letstalk.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class AuthViewModelImpl(
    private val authService: AuthService,
    private val tokenManager: TokenManager
): AuthViewModel, ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _authState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val authState: StateFlow<Resource<AuthResponse>?> = _authState

    fun onUserNameChange(value: String) {
        _userName.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    override fun register() {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            val result = authService.login(
                AuthRequest(
                    userName = _userName.value,
                    password = _password.value
                )
            )
            if (result is Resource.Success) {
                result.data?.token?.let { token ->
                    tokenManager.saveToken(token)
                }
            }
            _authState.value = result
        }
    }

    override fun login() {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            val result = authService.login(
                AuthRequest(
                    userName = _userName.value,
                    password = _password.value
                )
            )
            if (result is Resource.Success) {
                result.data?.token?.let { token ->
                    tokenManager.saveToken(token)
                }
            }
            _authState.value = result
        }
    }

    override fun checkAuthState(): Flow<String?> {
        return flow {
            emit(tokenManager.getToken())
        }
    }

    override fun logout() {
        viewModelScope.launch {
            tokenManager.deleteToken()
            _authState.value = null
        }
    }
}