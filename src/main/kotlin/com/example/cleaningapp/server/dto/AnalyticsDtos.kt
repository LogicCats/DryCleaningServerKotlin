package com.example.cleaningapp.server.dto


import jakarta.validation.constraints.NotBlank

data class AnalyticsEventRequest(
    val userId: Long?,          // nullable, если событие не привязано к конкретному юзеру
    @field:NotBlank(message = "eventType не должен быть пустым")
    val eventType: String,
    val details: String?        // произвольные детали
)

data class AnalyticsEventResponse(
    val id: Long,
    val userId: Long?,
    val eventType: String,
    val details: String?,
    val createdAt: String
)
