package com.example.letstalk.app.data.remote.viewModel

import kotlinx.coroutines.flow.Flow

interface AuthViewModel {
    fun register()

    fun login()

    fun checkAuthState(): Flow<String?>

    fun logout()
}