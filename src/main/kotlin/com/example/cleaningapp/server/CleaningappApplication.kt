package com.example.cleaningapp.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CleaningappApplication

fun main(args: Array<String>) {
	runApplication<CleaningappApplication>(*args)
}
