package com.example.cleaningapp.server.dto


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class OrderCreateRequest(
    @field:NotBlank(message = "Адрес не должен быть пустым")
    val address: String,

    @field:NotBlank(message = "Дата и время не должно быть пустым")
    val scheduledDateTime: String, // ISO-строка

    val promoCode: String? = null,

    @field:NotEmpty(message = "Нужно выбрать хотя бы одну услугу")
    val services: List<Int>,

    // Список изображений (multipart files). Если нет, то пустой список.
    val images: List<MultipartFile> = emptyList()
)

data class OrderUpdateRequest(
    val address: String?,
    val scheduledDateTime: String?,
    val promoCode: String?,
    val services: List<Int>?
)

data class OrderSummaryResponse(
    val id: String,
    val createdAt: String,
    val scheduledDateTime: String,
    val totalAmount: Double,
    val status: String
)

data class OrderDetailsResponse(
    val id: String,
    val userId: Long,
    val createdAt: String,
    val scheduledDateTime: String,
    val address: String,
    val promoCode: String?,
    val totalAmount: Double,
    val status: String,
    val services: List<Int>,
    val imageUrls: List<String>
)
