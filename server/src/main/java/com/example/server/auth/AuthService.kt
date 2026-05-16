package com.example.server.auth

import com.example.server.data.DataSource
import com.example.server.model.AuthRequest
import com.example.server.model.AuthResponse
import com.example.server.model.InvalidCredentialsException
import com.example.server.model.User
import com.example.server.model.UserAlreadyExistException
import com.example.server.model.UserDoesNotExistException
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val dataSource: DataSource
) {
    //Registering for the first time
    suspend fun register(request: AuthRequest): AuthResponse {
        val existingUser = dataSource.userExistsByUserName(request.userName)
        if (existingUser) throw UserAlreadyExistException()

        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())

        val user = User(
            userName = request.userName,
            password = hashedPassword,
            roomsId = emptyList()
        )

        dataSource.insertUser(user)

        val token = JwtConfig.generateToken(request.userName)
        return AuthResponse(token = token, message = "Registration successful")
    }

    suspend fun login(request: AuthRequest): AuthResponse {
        val user = dataSource.getUserByUserName(request.userName)
            ?: throw UserDoesNotExistException()

        val passwordMatch = BCrypt.checkpw(request.password, user.password)
        if (!passwordMatch) throw InvalidCredentialsException()

        val token = JwtConfig.generateToken(request.userName)
        return AuthResponse(token = token, message = "Login successful")
    }
}

