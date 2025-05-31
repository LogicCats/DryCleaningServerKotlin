package com.example.cleaningapp.server.controller


import com.example.cleaningapp.server.dto.AuthResponse
import com.example.cleaningapp.server.dto.LoginRequest
import com.example.cleaningapp.server.dto.RegisterRequest
import com.example.cleaningapp.server.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        val token: String = authService.registerUser(request)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        val token: String = authService.loginUser(request)
        return ResponseEntity.ok(AuthResponse(token))
    }
}
