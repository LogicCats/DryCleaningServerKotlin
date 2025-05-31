package com.example.cleaningapp.server.controller


import com.example.cleaningapp.server.dto.AnalyticsEventRequest
import com.example.cleaningapp.server.dto.AnalyticsEventResponse
import com.example.cleaningapp.server.service.AnalyticsService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/analytics")
class AnalyticsController(
    private val analyticsService: AnalyticsService
) {

    @PostMapping("/events")
    fun logEvent(@Valid @RequestBody request: AnalyticsEventRequest): ResponseEntity<AnalyticsEventResponse> {
        val saved = analyticsService.logEvent(request)
        return ResponseEntity.status(201).body(saved)
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/events/user/{userId}")
    fun getEventsForUser(@PathVariable userId: Long): ResponseEntity<List<AnalyticsEventResponse>> {
        val list = analyticsService.getEventsForUser(userId)
        return ResponseEntity.ok(list)
    }
}
