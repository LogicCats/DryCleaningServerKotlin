package com.example.cleaningapp.server.controller


import com.example.cleaningapp.server.dto.UserProfileResponse
import com.example.cleaningapp.server.dto.UserUpdateRequest
import com.example.cleaningapp.server.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun getProfile(
        @AuthenticationPrincipal principal: UserDetails
    ): ResponseEntity<UserProfileResponse> {
        val profile = userService.getProfile(principal.username)
        return ResponseEntity.ok(profile)
    }

    @PutMapping("/me")
    fun updateProfile(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody request: UserUpdateRequest
    ): ResponseEntity<UserProfileResponse> {
        val updated = userService.updateProfile(principal.username, request)
        return ResponseEntity.ok(updated)
    }
}
