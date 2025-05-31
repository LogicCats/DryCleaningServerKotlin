package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.Promotion
import org.springframework.data.jpa.repository.JpaRepository

interface PromotionRepository : JpaRepository<Promotion, Long> {
    fun findByCodeAndActiveTrue(code: String): Promotion?
    fun findAllByActiveTrue(): List<Promotion>
}
