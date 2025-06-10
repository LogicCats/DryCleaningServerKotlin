package com.example.cleaningapp.server.service


import com.example.cleaningapp.server.dto.OrderCreateRequest
import com.example.cleaningapp.server.dto.OrderDetailsResponse
import com.example.cleaningapp.server.dto.OrderSummaryResponse
import com.example.cleaningapp.server.entity.Order
import com.example.cleaningapp.server.entity.OrderImage
import com.example.cleaningapp.server.entity.Promotion
import com.example.cleaningapp.server.entity.User
import com.example.cleaningapp.server.repository.OrderImageRepository
import com.example.cleaningapp.server.repository.OrderRepository
import com.example.cleaningapp.server.repository.PromotionRepository
import com.example.cleaningapp.server.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderImageRepository: OrderImageRepository,
    private val userRepository: UserRepository,
    private val promotionRepository: PromotionRepository,
    private val fileStorageService: FileStorageService
) {

    @Transactional
    fun createOrder(username: String, request: OrderCreateRequest): OrderDetailsResponse {
        // 1) Получаем пользователя
        val user: User = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь не найден")

        // 2) Проверяем и получаем процент скидки (если есть)
        val discountPct: Double? = if (!request.promoCode.isNullOrBlank()) {
            val promo: Promotion = promotionRepository.findByCodeAndActiveTrue(request.promoCode!!)
                ?: throw IllegalArgumentException("Промокод не найден или неактивен")
            val now = LocalDateTime.now()
            val validFrom = promo.validFrom
            val validTo = promo.validTo
            if (validFrom  != null && validFrom.isAfter(now)) {
                throw IllegalArgumentException("Промокод ещё не доступен")
            }
            if (validTo != null && validTo.isBefore(now)) {
                throw IllegalArgumentException("Промокод истёк")
            }
            promo.discountPct
        } else {
            null
        }

        // 3) Считаем базовую сумму по списку услуг
        val baseAmount = request.services.sumOf { serviceId ->
            // Используем PromotionService.getServicePrice или повторяем здесь
            when (serviceId) {
                1 -> 500.0
                2 -> 400.0
                3 -> 300.0
                else -> throw IllegalArgumentException("Неизвестный ID услуги: $serviceId")
            }
        }

        // 4) Применяем скидку, если нужно
        val totalAmount = if (discountPct != null) {
            baseAmount * (1 - discountPct / 100.0)
        } else {
            baseAmount
        }

        // 5) Создаём сущность Order
        val order = Order(
            id = UUID.randomUUID(),
            user = user,
            createdAt = LocalDateTime.now(),
            scheduledDateTime = LocalDateTime.parse(request.scheduledDateTime),
            address = request.address,
            promoCode = request.promoCode,
            totalAmount = totalAmount,
            status = "CREATED",
            services = request.services.toMutableList()
        )

        orderRepository.save(order)

        // 6) Сохраняем изображения, если есть
        val savedFilenames: List<String> = if (request.images.isNotEmpty()) {
            fileStorageService.storeOrderImages(order.id.toString(), request.images)
        } else {
            emptyList()
        }

        // 7) Сохраняем записи в order_images
        savedFilenames.forEach { filename ->
            val imageEntity = OrderImage(
                order = order,
                filePath = filename,
                createdAt = LocalDateTime.now()
            )
            orderImageRepository.save(imageEntity)
        }

        // 8) Формируем публичные URL для картинок
        val imageUrls = savedFilenames.map { "/uploads/orders/$it" }

        // 9) Возвращаем DTO
        return OrderDetailsResponse(
            id = order.id.toString(),
            userId = user.id,
            createdAt = order.createdAt.toString(),
            scheduledDateTime = order.scheduledDateTime.toString(),
            address = order.address,
            promoCode = order.promoCode,
            totalAmount = order.totalAmount,
            status = order.status,
            services = order.services,
            imageUrls = imageUrls
        )
    }

    @Transactional(readOnly = true)
    fun getAllOrdersForUser(username: String): List<OrderSummaryResponse> {
        val user: User = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь не найден")
        val orders = orderRepository.findAllByUserIdOrderByCreatedAtDesc(user.id)
        return orders.map { order ->
            OrderSummaryResponse(
                id = order.id.toString(),
                createdAt = order.createdAt.toString(),
                scheduledDateTime = order.scheduledDateTime.toString(),
                totalAmount = order.totalAmount,
                status = order.status
            )
        }
    }

    @Transactional(readOnly = true)
    fun getOrderDetails(username: String, orderId: String): OrderDetailsResponse {
        val user: User = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь не найден")
        val order = orderRepository.findById(UUID.fromString(orderId))
            .orElseThrow { IllegalArgumentException("Заказ не найден") }
        if (order.user.id != user.id) {
            throw IllegalAccessException("Нет прав доступа к этому заказу")
        }

        val imageUrls = order.images.map { "/uploads/orders/${it.filePath}" }
        return OrderDetailsResponse(
            id = order.id.toString(),
            userId = user.id,
            createdAt = order.createdAt.toString(),
            scheduledDateTime = order.scheduledDateTime.toString(),
            address = order.address,
            promoCode = order.promoCode,
            totalAmount = order.totalAmount,
            status = order.status,
            services = order.services,
            imageUrls = imageUrls
        )
    }

    @Transactional(readOnly = true)
    fun searchOrders(
        username: String,
        query: String,
        pageable: Pageable
    ): Page<OrderSummaryResponse> {
        val user = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь не найден")

        val orders: Page<Order> = when {
            query.isBlank() -> orderRepository.findByUserId(user.id, pageable)

            query.length >= 8 -> orderRepository.findByUserIdAndIdStrContainingIgnoreCase(user.id, query, pageable)

            else -> orderRepository.findByUserIdAndAddressContainingIgnoreCase(user.id, query, pageable)
        }

        return orders.map { order ->
            OrderSummaryResponse(
                id = order.id.toString(),
                createdAt = order.createdAt.toString(),
                scheduledDateTime = order.scheduledDateTime.toString(),
                totalAmount = order.totalAmount,
                status = order.status
            )
        }
    }






    @Transactional
    fun updateOrder(
        username: String,
        orderId: String,
        request: com.example.cleaningapp.server.dto.OrderUpdateRequest
    ): OrderDetailsResponse {
        val user: User = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь не найден")
        val order = orderRepository.findById(UUID.fromString(orderId))
            .orElseThrow { IllegalArgumentException("Заказ не найден") }
        if (order.user.id != user.id) {
            throw IllegalAccessException("Нет прав доступа к этому заказу")
        }

        // Позволяем обновлять только до запланированного времени, если статус = CREATED
        if (order.status != "CREATED") {
            throw IllegalArgumentException("Нельзя обновлять заказ с текущим статусом")
        }

        request.address?.let { order.address = it }
        request.scheduledDateTime?.let { order.scheduledDateTime = LocalDateTime.parse(it) }
        request.promoCode?.let {
            // Аналогично проверяем промокод
            val discountPct = promotionRepository.findByCodeAndActiveTrue(it)
                ?: throw IllegalArgumentException("Промокод не найден или неактивен")
            // ...доп. логика пересчёта суммы
            order.promoCode = it
            // Обновляем totalAmount при необходимости
        }
        request.services?.let { order.services = it.toMutableList() }


        val saved = orderRepository.save(order)

        val imageUrls = saved.images.map { "/uploads/orders/${it.filePath}" }
        return OrderDetailsResponse(
            id = saved.id.toString(),
            userId = user.id,
            createdAt = saved.createdAt.toString(),
            scheduledDateTime = saved.scheduledDateTime.toString(),
            address = saved.address,
            promoCode = saved.promoCode,
            totalAmount = saved.totalAmount,
            status = saved.status,
            services = saved.services,
            imageUrls = imageUrls
        )
    }

    @Transactional
    fun deleteOrder(username: String, orderId: String) {
        val user: User = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Пользователь не найден")
        val orderUuid = UUID.fromString(orderId)
        val order = orderRepository.findById(orderUuid)
            .orElseThrow { IllegalArgumentException("Заказ не найден") }
        if (order.user.id != user.id) {
            throw IllegalAccessException("Нет прав доступа к этому заказу")
        }
        // Можно изменить статус или удалить запись:
        order.status = "CANCELED"
        orderRepository.save(order)
        // Если хотите физически удалять, замените на: orderRepository.delete(order)
    }
}
