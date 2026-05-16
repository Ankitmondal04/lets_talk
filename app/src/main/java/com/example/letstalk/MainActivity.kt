package com.example.letstalk

import NavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.letstalk.ui.theme.LetsTalkTheme
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.letstalk.app.data.local.TokenManager
import com.example.letstalk.app.service.AuthServiceImpl
import com.example.letstalk.app.service.MessageServiceImpl
import com.example.letstalk.app.service.RoomServiceImpl
import com.example.letstalk.app.service.WebSocketServiceImpl
import com.example.letstalk.app.view.viewmodel.AuthViewModelImpl
import com.example.letstalk.app.view.viewmodel.ChatViewModelImpl
import com.example.letstalk.app.view.viewmodel.HomeViewModelImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    // ─── Ktor HttpClient ───────────────────────────────────────
    private val client = HttpClient(OkHttp) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging)
    }

    // ─── Services ──────────────────────────────────────────────
    private val authService by lazy { AuthServiceImpl(client) }
    private val roomService by lazy { RoomServiceImpl(client) }
    private val messageService by lazy { MessageServiceImpl(client) }
    private val chatSocketService by lazy { WebSocketServiceImpl(client) }

    // ─── TokenManager ──────────────────────────────────────────
    private val tokenManager by lazy { TokenManager(this) }

    // ─── ViewModels ────────────────────────────────────────────
    private val authViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return AuthViewModelImpl(authService, tokenManager) as T
                }
            }
        )[AuthViewModelImpl::class.java]
    }

    private val homeViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModelImpl(roomService, tokenManager) as T
                }
            }
        )[HomeViewModelImpl::class.java]
    }

    private val chatViewModel by lazy {
        ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ChatViewModelImpl(messageService, chatSocketService, tokenManager) as T
                }
            }
        )[ChatViewModelImpl::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LetsTalkTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    authViewModel =  authViewModel,
                    homeViewModel =  homeViewModel,
                    chatViewModel = chatViewModel,
                    tokenManager =  tokenManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.close()
    }
}