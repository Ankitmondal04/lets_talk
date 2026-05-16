package com.example.server.plugins

import com.example.server.model.RoomAlreadyExistException
import com.example.server.model.RoomDoesNotExistException
import com.example.server.model.UserAlreadyExistException
import com.example.server.model.UserDoesNotExistException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<RoomDoesNotExistException> { call, _ ->
            call.respond(HttpStatusCode.NotFound, "Room does not exist")
        }
        exception<RoomAlreadyExistException> { call, _ ->
            call.respond(HttpStatusCode.Conflict, "Room already exists")
        }
        exception<UserDoesNotExistException> { call, _ ->
            call.respond(HttpStatusCode.NotFound, "User does not exist")
        }
        exception<UserAlreadyExistException> { call, _ ->
            call.respond(HttpStatusCode.Conflict, "User already exist")
        }
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Unknown error")
        }
    }
}