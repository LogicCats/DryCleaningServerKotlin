package com.example.cleaningapp.server.controller

import com.example.cleaningapp.server.dto.OrderCreateRequest
import com.example.cleaningapp.server.dto.OrderDetailsResponse
import com.example.cleaningapp.server.dto.OrderSummaryResponse
import com.example.cleaningapp.server.dto.OrderUpdateRequest
import com.example.cleaningapp.server.service.OrderService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
    private val objectMapper: ObjectMapper
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun createOrder(
        @AuthenticationPrincipal principal: UserDetails,
        @RequestPart("address") address: String,
        @RequestPart("scheduledDateTime") scheduledDateTime: String,
        @RequestPart("promoCode", required = false) promoCode: String?,
        @RequestPart("services") services: String,     // JSON-массив: "[1,2,3]"
        @RequestPart("images", required = false) images: List<MultipartFile>?
    ): ResponseEntity<OrderDetailsResponse> {
        val serviceList: List<Int> = objectMapper.readValue(
            services,
            object : TypeReference<List<Int>>() {}
        )
        val request = OrderCreateRequest(
            address = address,
            scheduledDateTime = scheduledDateTime,
            promoCode = promoCode,
            services = serviceList,
            images = images ?: emptyList()
        )
        val created = orderService.createOrder(principal.username, request)
        return ResponseEntity.ok(created)
    }

    @GetMapping
    fun getAllOrders(
        @AuthenticationPrincipal principal: UserDetails
    ): ResponseEntity<List<OrderSummaryResponse>> {
        val list = orderService.getAllOrdersForUser(principal.username)
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{orderId}")
    fun getOrderDetails(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable orderId: String
    ): ResponseEntity<OrderDetailsResponse> {
        val details = orderService.getOrderDetails(principal.username, orderId)
        return ResponseEntity.ok(details)
    }

    @GetMapping("/search")
    fun searchOrders(
        @AuthenticationPrincipal principal: UserDetails,
        @RequestParam("q") query: String,
        pageable: Pageable
    ): ResponseEntity<Page<OrderSummaryResponse>> {
        val page = orderService.searchOrders(principal.username, query, pageable)
        return ResponseEntity.ok(page)
    }

    @PutMapping("/{orderId}")
    fun updateOrder(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable orderId: String,
        @Valid @RequestBody request: OrderUpdateRequest
    ): ResponseEntity<OrderDetailsResponse> {
        val updated = orderService.updateOrder(principal.username, orderId, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable orderId: String
    ): ResponseEntity<Void> {
        orderService.deleteOrder(principal.username, orderId)
        return ResponseEntity.noContent().build()
    }
}
