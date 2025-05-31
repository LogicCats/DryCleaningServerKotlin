package com.example.cleaningapp.server.service


import com.example.cleaningapp.server.entity.Promotion
import com.example.cleaningapp.server.repository.PromotionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import com.example.cleaningapp.server.dto.AnalyticsEventRequest
import com.example.cleaningapp.server.dto.AnalyticsEventResponse
import com.example.cleaningapp.server.entity.AnalyticsEvent
import com.example.cleaningapp.server.entity.User
import com.example.cleaningapp.server.repository.AnalyticsEventRepository
import com.example.cleaningapp.server.repository.UserRepository
import java.time.format.DateTimeFormatter

@Service
class AnalyticsService(
    private val analyticsEventRepository: AnalyticsEventRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun logEvent(request: AnalyticsEventRequest): AnalyticsEventResponse {
        val user: User? = request.userId?.let { userId ->
            userRepository.findById(userId).orElse(null)
        }
        val entity = AnalyticsEvent(
            user = user,
            eventType = request.eventType,
            details = request.details
        )
        val saved = analyticsEventRepository.save(entity)
        return AnalyticsEventResponse(
            id = saved.id,
            userId = saved.user?.id,
            eventType = saved.eventType,
            details = saved.details,
            createdAt = saved.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    @Transactional(readOnly = true)
    fun getEventsForUser(userId: Long): List<AnalyticsEventResponse> {
        val events = analyticsEventRepository.findAllByUserId(userId)
        return events.map { ev ->
            AnalyticsEventResponse(
                id = ev.id,
                userId = ev.user?.id,
                eventType = ev.eventType,
                details = ev.details,
                createdAt = ev.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
        }
    }
}
