package com.example.cleaningapp.server.dto


import jakarta.validation.constraints.*

data class PromotionCreateRequest(
    @field:NotBlank(message = "Код не должен быть пустым")
    val code: String,

    @field:NotBlank(message = "Название не должно быть пустым")
    val title: String,

    val description: String? = null,

    @field:DecimalMin(value = "0.0", message = "Процент не может быть меньше 0")
    @field:DecimalMax(value = "100.0", message = "Процент не может быть больше 100")
    val discountPct: Double,

    // ISO 8601 или null
    val validFrom: String? = null,
    val validTo: String? = null
)

data class PromotionUpdateRequest(
    val title: String?,
    val description: String?,
    val discountPct: Double?,
    val validFrom: String?,
    val validTo: String?,
    val active: Boolean?
)

data class PromotionResponse(
    val id: Long,
    val code: String,
    val title: String,
    val description: String?,
    val discountPct: Double,
    val active: Boolean,
    val validFrom: String?,
    val validTo: String?
)
