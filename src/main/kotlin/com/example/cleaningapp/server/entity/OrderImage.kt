package com.example.cleaningapp.server.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "order_images")
class OrderImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @Column(name = "file_path", nullable = false)
    var filePath: String,  // имя файла в папке uploads/orders/

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = 0,
        order = Order(),
        filePath = "",
        createdAt = LocalDateTime.now()
    )
}
