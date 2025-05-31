package com.example.cleaningapp.server.controller


import com.example.cleaningapp.server.dto.PromotionCreateRequest
import com.example.cleaningapp.server.dto.PromotionResponse
import com.example.cleaningapp.server.dto.PromotionUpdateRequest
import com.example.cleaningapp.server.service.PromotionService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/promotions")
class PromotionController(
    private val promotionService: PromotionService
) {

    @GetMapping
    fun getAllActive(): ResponseEntity<List<PromotionResponse>> {
        val promos = promotionService.getAllActivePromotions().map { promo ->
            PromotionResponse(
                id = promo.id,
                code = promo.code,
                title = promo.title,
                description = promo.description,
                discountPct = promo.discountPct,
                active = promo.active,
                validFrom = promo.validFrom?.toString(),
                validTo = promo.validTo?.toString()
            )
        }
        return ResponseEntity.ok(promos)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    fun create(@Valid @RequestBody request: PromotionCreateRequest): ResponseEntity<PromotionResponse> {
        val promo = promotionService.createPromotion(request)
        val response = PromotionResponse(
            id = promo.id,
            code = promo.code,
            title = promo.title,
            description = promo.description,
            discountPct = promo.discountPct,
            active = promo.active,
            validFrom = promo.validFrom?.toString(),
            validTo = promo.validTo?.toString()
        )
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: PromotionUpdateRequest
    ): ResponseEntity<PromotionResponse> {
        val updated = promotionService.updatePromotion(id, request)
        val response = PromotionResponse(
            id = updated.id,
            code = updated.code,
            title = updated.title,
            description = updated.description,
            discountPct = updated.discountPct,
            active = updated.active,
            validFrom = updated.validFrom?.toString(),
            validTo = updated.validTo?.toString()
        )
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        promotionService.deletePromotion(id)
        return ResponseEntity.noContent().build()
    }
}
