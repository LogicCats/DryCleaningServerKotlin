package com.example.cleaningapp.server.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "promotions",
    uniqueConstraints = [UniqueConstraint(columnNames = ["code"])]
)
data class Promotion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true)
    var code: String,                // код промокода

    @Column(nullable = false)
    var title: String,               // название акции

    @Column(columnDefinition = "TEXT")
    var description: String? = null,  // описание акции

    @Column(name = "discount_pct", nullable = false)
    var discountPct: Double,         // процент скидки (0.0–100.0)

    @Column(nullable = false)
    var active: Boolean = true,      // активна ли акция

    @Column(name = "valid_from")
    var validFrom: LocalDateTime? = null, // дата начала

    @Column(name = "valid_to")
    var validTo: LocalDateTime? = null    // дата окончания
) {
    constructor() : this(
        id = 0,
        code = "",
        title = "",
        description = null,
        discountPct = 0.0,
        active = true,
        validFrom = null,
        validTo = null
    )
}
