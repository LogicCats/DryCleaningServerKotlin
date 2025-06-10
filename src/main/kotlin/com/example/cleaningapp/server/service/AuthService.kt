package com.example.cleaningapp.server.service


import com.example.cleaningapp.server.config.JwtUtil
import com.example.cleaningapp.server.dto.LoginRequest
import com.example.cleaningapp.server.dto.RegisterRequest
import com.example.cleaningapp.server.entity.Role
import com.example.cleaningapp.server.entity.User
import com.example.cleaningapp.server.repository.RoleRepository
import com.example.cleaningapp.server.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @Transactional
    fun registerUser(request: RegisterRequest): String {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email уже используется")
        }
        val hashedPassword = passwordEncoder.encode(request.password)
        val userRole: Role = roleRepository.findByName("ROLE_USER")
            ?: throw IllegalStateException("Role ROLE_USER не найдена")

        val user = User(
            email = request.email,
            passwordHash = hashedPassword,
            name = request.name,
            phone = request.phone,
            createdAt = LocalDateTime.now(),
            roles = mutableSetOf(userRole)
        )
        userRepository.save(user)

        // После сохранения – генерируем JWT
        return jwtUtil.generateToken(user.email, listOf(userRole.name))
    }

    @Transactional(readOnly = true)
    fun loginUser(request: LoginRequest): String {
        try {
            val authToken = UsernamePasswordAuthenticationToken(request.email, request.password)
            authenticationManager.authenticate(authToken)
        } catch (ex: AuthenticationException) {
            throw IllegalArgumentException("Неверный email или пароль")
        }
        // Успех → достаём роли и формируем JWT
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Пользователь не найден")
        val roles = user.roles.map { it.name }
        return jwtUtil.generateToken(user.email, roles)
    }
}
