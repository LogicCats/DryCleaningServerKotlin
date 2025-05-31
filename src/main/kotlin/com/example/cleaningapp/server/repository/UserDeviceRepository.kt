package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.UserDevice
import org.springframework.data.jpa.repository.JpaRepository

interface UserDeviceRepository : JpaRepository<UserDevice, Long> {
    fun findAllByUserId(userId: Long): List<UserDevice>
    fun findByDeviceToken(deviceToken: String): UserDevice?
}
