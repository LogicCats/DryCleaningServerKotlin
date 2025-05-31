package com.example.cleaningapp.server.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "analytics_events")
class AnalyticsEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null, // nullable

    @Column(name = "event_type", nullable = false)
    var eventType: String,

    @Column(columnDefinition = "TEXT")
    var details: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = 0,
        user = null,
        eventType = "",
        details = null,
        createdAt = LocalDateTime.now()
    )
}
