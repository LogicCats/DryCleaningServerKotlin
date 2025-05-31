package com.example.cleaningapp.server.repository


import com.example.cleaningapp.server.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}
