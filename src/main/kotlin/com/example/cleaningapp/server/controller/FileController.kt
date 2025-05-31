package com.example.cleaningapp.server.controller


import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.MalformedURLException
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/api/files")
class FileController(
    @Value("\${app.upload.dir}") private val uploadDir: String
) {

    @GetMapping("/orders/{filename:.+}")
    fun getOrderImage(@PathVariable filename: String): ResponseEntity<Resource> {
        val filePath: Path = Paths.get(uploadDir).resolve("orders").resolve(filename)
        val resource: Resource = try {
            UrlResource(filePath.toUri())
        } catch (ex: MalformedURLException) {
            return ResponseEntity.notFound().build()
        }

        return if (resource.exists() && resource.isReadable) {
            ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
                .body(resource)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
