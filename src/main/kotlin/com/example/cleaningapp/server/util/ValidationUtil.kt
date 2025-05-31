package com.example.cleaningapp.server.util


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object ValidationUtil {
    /**
     * Проверяет, что строка соответствует формату ISO_LOCAL_DATE_TIME (например, "2025-06-01T14:30:00").
     */
    @JvmStatic
    fun isValidIsoDateTime(value: String): Boolean {
        return try {
            LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            true
        } catch (ex: DateTimeParseException) {
            false
        }
    }
}
