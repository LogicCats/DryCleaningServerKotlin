package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.OrderImage
import org.hibernate.validator.constraints.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface OrderImageRepository : JpaRepository<OrderImage, Long> {
    fun findAllByOrderId(orderId: UUID): List<OrderImage>
}
