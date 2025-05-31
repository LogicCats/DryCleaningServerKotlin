package com.example.cleaningapp.server.dto


import jakarta.validation.constraints.NotBlank

data class UserProfileResponse(
    val id: Long,
    val email: String,
    val name: String,
    val phone: String,
    val createdAt: String // ISO-строка (например, "2025-06-01T14:30:00")
)

data class UserUpdateRequest(
    @field:NotBlank(message = "Имя не должно быть пустым")
    val name: String,

    @field:NotBlank(message = "Телефон не должен быть пустым")
    val phone: String
)
