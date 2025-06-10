package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): List<Order>

    // поиск по части idStr
    fun findByUserIdAndIdStrContainingIgnoreCase(
        userId: Long,
        idPart: String,
        pageable: Pageable
    ): Page<Order>

    fun findByUserIdAndAddressContainingIgnoreCase(
        userId: Long,
        address: String,
        pageable: Pageable
    ): Page<Order>

    fun findByUserIdAndId(userId: Long, id: UUID, pageable: Pageable): Page<Order>

    fun findByUserId(userId: Long, pageable: Pageable): Page<Order>


}

