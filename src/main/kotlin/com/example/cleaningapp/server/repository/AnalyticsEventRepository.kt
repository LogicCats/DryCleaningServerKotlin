package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.AnalyticsEvent
import org.springframework.data.jpa.repository.JpaRepository

interface AnalyticsEventRepository : JpaRepository<AnalyticsEvent, Long> {
    fun findAllByUserId(userId: Long): List<AnalyticsEvent>
}
