package com.example.cleaningapp.server.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,

    @Value("\${jwt.expiration-ms}")
    private val jwtExpirationMs: Long
) {

    private lateinit var signingKey: Key

    @PostConstruct
    fun init() {
        val decoded: ByteArray = Base64.getDecoder().decode(jwtSecret)
        signingKey = Keys.hmacShaKeyFor(decoded)
    }

    @PostConstruct
    fun checkSecret() {
        check(!jwtSecret.isNullOrEmpty()) { "JWT secret is not set! Please configure JWT_SECRET env variable." }

    }

    fun generateToken(userEmail: String, roles: List<String> = emptyList()): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)
        return Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("roles", roles)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserEmailFromJwt(token: String): String {
        return extractClaims(token).subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token)
            true
        } catch (ex: JwtException) {
            false
        } catch (ex: IllegalArgumentException) {
            false
        }
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
