package com.example.cleaningapp.server.service

import com.example.cleaningapp.server.dto.UserProfileResponse
import com.example.cleaningapp.server.dto.UserUpdateRequest
import com.example.cleaningapp.server.entity.User
import com.example.cleaningapp.server.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getProfile(userEmail: String): UserProfileResponse {
        val user: User = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("Пользователь не найден")
        return UserProfileResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            phone = user.phone,
            createdAt = user.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }

    @Transactional
    fun updateProfile(userEmail: String, request: UserUpdateRequest): UserProfileResponse {
        val user: User = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("Пользователь не найден")
        user.name = request.name
        user.phone = request.phone
        userRepository.save(user)

        return UserProfileResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            phone = user.phone,
            createdAt = user.createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
    }
}
