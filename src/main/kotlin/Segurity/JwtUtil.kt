package com.example.cronograma.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

class JwtUtil {

    private val SECRET = "mi_secreto_muy_largo_para_jwt_123456789"

    private val key: SecretKey = Keys.hmacShaKeyFor(SECRET.toByteArray())

    fun generarToken(email: String): String {

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 86400000))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}