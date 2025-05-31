package com.example.cleaningapp.server.service


import com.example.cleaningapp.server.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Пользователь с email = $username не найден")

        val authorities: List<GrantedAuthority> = user.roles.map { role ->
            SimpleGrantedAuthority(role.name)
        }

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.passwordHash,
            authorities
        )
    }
}
