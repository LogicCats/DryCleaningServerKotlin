package com.example.cleaningapp.server.dto


import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:Email(message = "Email должен быть корректным")
    @field:NotBlank(message = "Email не должен быть пустым")
    val email: String,

    @field:Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    val password: String,

    @field:NotBlank(message = "Имя не должно быть пустым")
    val name: String,

    @field:NotBlank(message = "Телефон не должен быть пустым")
    val phone: String
)

data class LoginRequest(
    @field:Email(message = "Email должен быть корректным")
    @field:NotBlank(message = "Email не должен быть пустым")
    val email: String,

    @field:NotBlank(message = "Пароль не должен быть пустым")
    val password: String
)

data class AuthResponse(
    val token: String,
    val tokenType: String = "Bearer"
)
