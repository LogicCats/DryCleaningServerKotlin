package com.example.cleaningapp.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true)
    var name: String // например, "ROLE_USER"
) {
    constructor() : this(id = 0, name = "")
}
