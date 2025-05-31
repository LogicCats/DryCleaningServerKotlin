package com.example.cleaningapp.server.service

import com.example.cleaningapp.server.dto.PromotionCreateRequest
import com.example.cleaningapp.server.dto.PromotionUpdateRequest
import com.example.cleaningapp.server.entity.Promotion
import com.example.cleaningapp.server.repository.PromotionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PromotionService(
    private val promotionRepository: PromotionRepository
) {

    @Transactional(readOnly = true)
    fun validateAndGetDiscount(promoCode: String?): Double? {
        if (promoCode.isNullOrBlank()) return null
        val promo: Promotion = promotionRepository.findByCodeAndActiveTrue(promoCode)
            ?: throw IllegalArgumentException("Промокод не найден или неактивен")

        val now = LocalDateTime.now()
        if (promo.validFrom != null && promo.validFrom!!.isAfter(now)) {
            throw IllegalArgumentException("Промокод ещё не активен")
        }
        if (promo.validTo != null && promo.validTo!!.isBefore(now)) {
            throw IllegalArgumentException("Промокод истёк")
        }
        return promo.discountPct
    }

    fun getServicePrice(serviceId: Int): Double {
        return when (serviceId) {
            1 -> 500.0
            2 -> 400.0
            3 -> 300.0
            else -> throw IllegalArgumentException("Неизвестный ID услуги: $serviceId")
        }
    }

    @Transactional(readOnly = true)
    fun getAllActivePromotions(): List<Promotion> {
        return promotionRepository.findAllByActiveTrue()
    }

    @Transactional
    fun createPromotion(request: PromotionCreateRequest): Promotion {
        if (promotionRepository.findByCodeAndActiveTrue(request.code) != null) {
            throw IllegalArgumentException("Промокод с таким кодом уже существует")
        }
        val validFrom = request.validFrom?.let { LocalDateTime.parse(it) }
        val validTo = request.validTo?.let { LocalDateTime.parse(it) }
        val promo = Promotion(
            code = request.code,
            title = request.title,
            description = request.description,
            discountPct = request.discountPct,
            active = true,
            validFrom = validFrom,
            validTo = validTo
        )
        return promotionRepository.save(promo)
    }

    @Transactional
    fun updatePromotion(
        id: Long,
        request: PromotionUpdateRequest
    ): Promotion {
        val promo = promotionRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Акция не найдена") }

        request.title?.let { promo.title = it }
        request.description?.let { promo.description = it }
        request.discountPct?.let { promo.discountPct = it }
        request.validFrom?.let { promo.validFrom = LocalDateTime.parse(it) }
        request.validTo?.let { promo.validTo = LocalDateTime.parse(it) }
        request.active?.let { promo.active = it }

        return promotionRepository.save(promo)
    }

    @Transactional
    fun deletePromotion(id: Long) {
        if (!promotionRepository.existsById(id)) {
            throw IllegalArgumentException("Акция не найдена")
        }
        promotionRepository.deleteById(id)
    }
}
