package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): List<Order>
    fun findByUserIdAndIdContainingIgnoreCase(
        userId: Long,
        idPart: String,
        pageable: Pageable
    ): Page<Order>
}
