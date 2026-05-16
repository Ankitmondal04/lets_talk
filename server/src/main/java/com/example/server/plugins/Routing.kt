package com.example.server.plugins

import com.example.server.auth.AuthService
import com.example.server.data.DataSource
import com.example.server.model.AuthRequest
import com.example.server.model.NoParameterGivenException
import com.example.server.room.RoomController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting(
    dataSource: DataSource,
    roomController: RoomController,
    authService: AuthService
) {
    routing {

        get("/") {
            call.respondText("Server is running")
        }

        // ─── Auth routes (public) ──────────────────────────────
        post("/auth/register") {
            val request = call.receive<AuthRequest>()
            val response = authService.register(request)
            call.respond(HttpStatusCode.Created, response)
        }

        post("/auth/login") {
            val request = call.receive<AuthRequest>()
            val response = authService.login(request)
            call.respond(HttpStatusCode.OK, response)
        }

        // ─── Protected routes ──────────────────────────────────
        authenticate("auth-jwt") {

            get("/allMessages") {
                val roomLabel = call.request.queryParameters["roomLabel"]
                    ?: throw NoParameterGivenException()
                val roomId = dataSource.getRoomIdByLabel(roomLabel)
                val messages = dataSource.getAllMessagesByRoom(roomId)
                call.respond(HttpStatusCode.OK, messages)
            }

            get("/room/allMembers") {
                val roomLabel = call.request.queryParameters["roomLabel"]
                    ?: throw NoParameterGivenException()
                val roomId = dataSource.getRoomIdByLabel(roomLabel)
                val members = dataSource.getAllMemberOfRoom(roomId)
                call.respond(HttpStatusCode.OK, members)
            }

//            get("/room/all") {
//                val principal = call.principal<JWTPrincipal>()
//                val userName = principal?.payload?.getClaim("userName")?.asString()
//                    ?: throw NoParameterGivenException()
//                val rooms = dataSource.getAllRoomsByUser(userName)
//                call.respond(HttpStatusCode.OK, rooms)
//            }

            post("/room/create") {
                val principal = call.principal<JWTPrincipal>()
                val userName = principal?.payload?.getClaim("userName")?.asString()
                    ?: throw NoParameterGivenException()
                val roomLabel = call.request.queryParameters["roomLabel"]
                    ?: throw NoParameterGivenException()
                roomController.createRoom(roomLabel, userName)
                call.respond(HttpStatusCode.Created, "Room created successfully")
            }

            delete("/room/leave") {
                val principal = call.principal<JWTPrincipal>()
                val userName = principal?.payload?.getClaim("userName")?.asString()
                    ?: throw NoParameterGivenException()
                val roomLabel = call.request.queryParameters["roomLabel"]
                    ?: throw NoParameterGivenException()
                val roomId = dataSource.getRoomIdByLabel(roomLabel)
                dataSource.leaveRoom(roomId, userName)
                call.respond(HttpStatusCode.OK, "Left room successfully")
            }

//            delete("/room/delete") {
//                val principal = call.principal<JWTPrincipal>()
//                val userName = principal?.payload?.getClaim("userName")?.asString()
//                    ?: throw NoParameterGivenException()
//                val roomLabel = call.request.queryParameters["roomLabel"]
//                    ?: throw NoParameterGivenException()
//                roomController.deleteRoom(roomLabel, userName)
//                call.respond(HttpStatusCode.OK, "Room deleted successfully")
//            }
        }
    }
}