package com.example.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    const val SECRET = "letstalk_secret"
    const val ISSUER = "letstalk"
    const val AUDIENCE = "letstalk_users"
    const val REALM = "letstalk_app"
    private const val VALIDITY = 36_000_00L * 24L

    fun generateToken(userName: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userName", userName)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY))
            .sign(Algorithm.HMAC256(SECRET))
    }
}