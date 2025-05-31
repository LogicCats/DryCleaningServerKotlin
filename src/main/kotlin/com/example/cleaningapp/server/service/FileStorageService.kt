package com.example.cleaningapp.server.service


import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class FileStorageService(
    @Value("\${app.upload.dir}") private val uploadDir: String
) {

    init {
        // Создаём директории, если их нет
        val basePath = Paths.get(uploadDir)
        if (!Files.exists(basePath)) {
            Files.createDirectories(basePath)
        }
        val ordersPath = basePath.resolve("orders")
        if (!Files.exists(ordersPath)) {
            Files.createDirectories(ordersPath)
        }
    }

    /**
     * Сохраняет список файлов в папку /uploads/orders/
     * и возвращает список сгенерированных имён файлов.
     */
    fun storeOrderImages(orderId: String, files: List<MultipartFile>): List<String> {
        val savedFilenames = mutableListOf<String>()
        val ordersDir = Paths.get(uploadDir, "orders")

        for (file in files) {
            if (file.isEmpty) continue

            val originalName = file.originalFilename ?: "image"
            val extension = originalName.substringAfterLast('.', "jpg")
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            val uuid = UUID.randomUUID().toString().replace("-", "")
            val filename = "${orderId}_${timestamp}_${uuid}.$extension"

            val targetLocation: Path = ordersDir.resolve(filename)
            try {
                file.transferTo(targetLocation)
                savedFilenames.add(filename)
            } catch (ex: IOException) {
                throw RuntimeException("Не удалось сохранить файл $filename: ${ex.message}", ex)
            }
        }
        return savedFilenames
    }
}
