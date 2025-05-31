package com.example.cleaningapp.server.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_devices")
class UserDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "device_token", nullable = false, unique = true)
    var deviceToken: String,    // FCM-токен

    @Column(nullable = false)
    var platform: String        // например, "ANDROID" или "IOS"
) {
    constructor() : this(
        id = 0,
        user = User(),
        deviceToken = "",
        platform = ""
    )
}
