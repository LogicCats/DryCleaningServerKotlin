package com.example.cleaningapp.server.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var phone: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = HashSet()
) {
    constructor() : this(
        id = 0,
        email = "",
        passwordHash = "",
        name = "",
        phone = "",
        createdAt = LocalDateTime.now(),
        roles = HashSet()
    )
}
