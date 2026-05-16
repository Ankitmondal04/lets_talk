package com.example.server.data

import com.example.server.model.Message
import com.example.server.model.Room
import com.example.server.model.RoomDoesNotExistException
import com.example.server.model.User
import com.example.server.model.UserDoesNotExistException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class DataSourceImpl(
    private val db: MongoDatabase
): DataSource {

    private val allMessages: MongoCollection<Message> = db.getCollection<Message>("messages")
    private val allMembers: MongoCollection<User> = db.getCollection<User>("users")
    private val allRooms: MongoCollection<Room> = db.getCollection<Room>("rooms")


    override suspend fun getAllMessagesByRoom(roomId: String): List<Message> {
        if(!roomExists(roomId)) throw RoomDoesNotExistException()//Throws RoomDoesNotExistsException if there exists no room

        return allMessages.find(Filters.eq(Message::roomId.name, roomId)).sort(Sorts.ascending(
            Message::timeStamp.name)).toList()
    }

    override suspend fun insertMessage(message: Message) {
        allMessages.insertOne(message)
    }

    override suspend fun getAllMemberOfRoom(roomId: String): List<User> {
        if(!roomExists(roomId)) throw RoomDoesNotExistException()

        return allMembers.find(Filters.eq(User::roomsId.name, roomId)).toList()
    }

    override suspend fun insertUser(user: User) {
        allMembers.insertOne(user)
    }

    override suspend fun getUserByUserName(userName: String): User? {
        return allMembers.find(Filters.eq(User::userName.name, userName)).firstOrNull()

    }

    override suspend fun userExistsByUserName(userName: String): Boolean {
        return allMembers.find(Filters.eq(User::userName.name, userName))
            .firstOrNull() != null
    }

    override suspend fun getRoomIdByLabel(roomLabel: String): String {
        val roomId = allRooms.find(Filters.eq(Room::label.name, roomLabel))
            .firstOrNull()?.id ?: throw RoomDoesNotExistException()

        return roomId
    }

    override suspend fun insertRoom(room: Room) {

        allRooms.insertOne(room)
    }

    override suspend fun addRoomToUser(userName: String, roomId: String) {
        allMembers.updateOne(
            Filters.eq(User::userName.name, userName),
            Updates.addToSet(User::roomsId.name, roomId)
            )
    }

    override suspend fun leaveRoom(roomId: String, userName: String) {
        if(!userExistsByUserName(userName)) throw UserDoesNotExistException()
        if(!roomExists(roomId)) throw RoomDoesNotExistException()

        allMembers.updateOne(
            Filters.eq(User::userName.name, userName),
            Updates.pull(User::roomsId.name, roomId)
        )
    }

    override suspend fun deleteRoom(roomId: String) {
        allMembers.updateMany(
            Filters.eq(User::roomsId.name, roomId),
            Updates.pull(User::roomsId.name, roomId)
        )

        allMessages.deleteMany(Filters.eq(Message::roomId.name, roomId))

        allRooms.deleteOne(Filters.eq(Room::id.name, roomId))
    }

    override suspend fun roomExists(roomId: String): Boolean {
        return allRooms.find(
            Filters.eq(Room::id.name, roomId)
        ).firstOrNull() != null
    }

    override suspend fun roomExistsByLabel(roomLabel: String): Boolean {
        return allRooms.find(
            Filters.eq(Room::label.name, roomLabel)
        ).firstOrNull() != null
    }
}