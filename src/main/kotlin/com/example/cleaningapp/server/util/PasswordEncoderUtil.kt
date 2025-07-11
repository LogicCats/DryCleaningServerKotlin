package com.example.cleaningapp.server.util


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

object PasswordEncoderUtil {
    @JvmStatic
    fun bcryptEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
