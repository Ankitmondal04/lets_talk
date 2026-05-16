package com.example.server.plugins

import com.example.server.room.RoomController
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import java.time.Duration

fun Application.configureWebSocket(
    roomController: RoomController
) {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        authenticate("auth-jwt") {
            webSocket("/chat") {
                val userName = call.request.queryParameters["userName"]
                    ?: return@webSocket
                val roomLabel = call.request.queryParameters["roomLabel"]
                    ?: return@webSocket

                try {
                    roomController.onJoin(
                        userName = userName,
                        roomLabel = roomLabel,
                        socketId = this
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@webSocket
                }

                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            roomController.sendMessage(
                                userName = userName,
                                message = frame.readText(),
                                roomLabel = roomLabel
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    roomController.tryDisconnect(
                        userName = userName,
                        roomLabel = roomLabel
                    )
                }
            }
        }
    }
}