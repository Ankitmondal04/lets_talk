package com.example.server

import com.example.server.auth.AuthService
import com.example.server.data.DataSourceImpl
import com.example.server.plugins.configureCORS
import com.example.server.plugins.configureRouting
import com.example.server.plugins.configureSecurity
import com.example.server.plugins.configureSerialization
import com.example.server.plugins.configureStatusPage
import com.example.server.plugins.configureWebSocket
import com.example.server.room.RoomControllerImpl
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {

    val mongoClient = MongoClient.create("mongodb://127.0.0.1:27017")
    val db = mongoClient.getDatabase("letsTalk_db")

    val dataSource = DataSourceImpl(db)
    val roomController = RoomControllerImpl(dataSource)
    val authService = AuthService(dataSource)


    embeddedServer(Netty, port = 8080) {
        configureSerialization()
        configureStatusPage()
        configureCORS()
        configureSecurity()
        configureRouting(
            dataSource = dataSource,
            roomController = roomController,
            authService = authService
        )
        configureWebSocket(roomController)
    }.start(wait = true)
}