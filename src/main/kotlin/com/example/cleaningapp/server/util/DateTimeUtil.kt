package com.example.cleaningapp.server.util


import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    private val formatterIso = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Преобразует строку ISO (например, "2025-06-01T14:30:00") в LocalDateTime.
     */
    @JvmStatic
    fun parseIsoToLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value, formatterIso)
    }

    /**
     * Форматирует LocalDateTime в строку ISO.
     */
    @JvmStatic
    fun formatLocalDateTime(value: LocalDateTime): String {
        return value.format(formatterIso)
    }
}
