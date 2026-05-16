package com.example.server.room

import com.example.server.data.DataSource
import com.example.server.model.Message
import com.example.server.model.Room
import com.example.server.model.RoomAlreadyExistException
import com.example.server.model.RoomDoesNotExistException
import com.example.server.model.UserAlreadyExistException
import com.example.server.model.UserDoesNotExistException
import com.example.server.model.UserSession
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomControllerImpl(
    private val dataSource: DataSource
): RoomController {

    private val membersOfRoom = ConcurrentHashMap<String, ConcurrentHashMap<String, UserSession>>()

    override suspend fun onJoin(
        userName: String,
        roomLabel: String,
        socketId: WebSocketSession
    ) {
        val roomId = dataSource.getRoomIdByLabel(roomLabel)

        dataSource.getUserByUserName(userName)?:throw UserDoesNotExistException()

        val roomSessions = membersOfRoom.getOrPut(roomId) { ConcurrentHashMap() }

        if (roomSessions.containsKey(userName)) throw UserAlreadyExistException()

        roomSessions[userName] = UserSession(userName, socketId)

        dataSource.addRoomToUser(userName, roomId)

    }

    override suspend fun sendMessage(
        message: String,
        userName: String,
        roomLabel: String
    ) {
        val roomId = dataSource.getRoomIdByLabel(roomLabel)

        val messageEntity = Message(
            text = message,
            timeStamp = System.currentTimeMillis(),
            userName = userName,
            roomId = roomId
        )

        dataSource.insertMessage(messageEntity)

        val roomMembers = membersOfRoom[roomId]?: throw RoomDoesNotExistException()

        roomMembers.values.forEach { memberSession ->
            val frameText = Json.encodeToString(Message.serializer(), messageEntity)
            memberSession.session.send(Frame.Text(frameText))
        }
    }

    override suspend fun createRoom(roomLabel: String, owner: String) {
        if(dataSource.roomExistsByLabel(roomLabel)) throw RoomAlreadyExistException()

        val user = dataSource.getUserByUserName(owner)?:throw UserDoesNotExistException()

        val room = Room(roomLabel, user.id)

        dataSource.insertRoom(room)

        dataSource.addRoomToUser(owner, room.id)

    }

    override suspend fun tryDisconnect(userName: String, roomLabel: String) {
        val roomId = dataSource.getRoomIdByLabel(roomLabel)

        val roomMembers = membersOfRoom[roomId]?:return

        roomMembers.remove(userName)

        if(roomMembers.isEmpty()) {
            membersOfRoom.remove(roomId)
        }
    }

}