package com.example.cleaningapp.server.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @Column(columnDefinition = "UUID")
    var id: UUID = UUID.randomUUID(),


    @Column(name = "id_str")
    val idStr: String = id.toString(),


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "scheduled_datetime", nullable = false)
    var scheduledDateTime: LocalDateTime,

    @Column(nullable = false)
    var address: String,

    @Column(name = "promo_code")
    var promoCode: String? = null,

    @Column(name = "total_amount", nullable = false)
    var totalAmount: Double,

    @Column(nullable = false)
    var status: String = "CREATED",

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "order_services",
        joinColumns = [JoinColumn(name = "order_id")]
    )
    @Column(name = "service_id")
    var services: MutableList<Int> = mutableListOf(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var images: MutableList<OrderImage> = mutableListOf()
) {
    constructor() : this(
        id = UUID.randomUUID(),
        idStr = UUID.randomUUID().toString(), // для пустого конструктора
        user = User(),
        createdAt = LocalDateTime.now(),
        scheduledDateTime = LocalDateTime.now(),
        address = "",
        promoCode = null,
        totalAmount = 0.0,
        status = "CREATED",
        services = mutableListOf(),
        images = mutableListOf()
    )
}

