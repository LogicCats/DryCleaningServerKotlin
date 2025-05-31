plugins {
	id("org.springframework.boot") version "3.4.6"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.9.10"
	kotlin("plugin.spring") version "1.9.10"
	kotlin("plugin.jpa") version "1.9.10"
}

group = "com.example.cleaningapp.server"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
	mavenCentral()
}

dependencies {
	// 1) Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-web")          // обработка HTTP-запросов, Jackson, Tomcat embedded
	implementation("org.springframework.boot:spring-boot-starter-security")     // Spring Security
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")     // JPA + Hibernate
	implementation("org.springframework.boot:spring-boot-starter-validation")   // Bean Validation (Jakarta Validation)

	// 2) JWT-библиотека (JJWT)
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// 3) Liquibase для миграций
	implementation("org.liquibase:liquibase-core")

	// 4) Драйвер базы данных (PostgreSQL или H2)
	runtimeOnly("org.postgresql:postgresql")       // если вы будете разворачивать на Postgres
	// runtimeOnly("com.h2database:h2")            // или для локальной разработки (in-memory)

	// 5) Azure Support (можно удалить, если пока не нужен)
	implementation("com.azure:azure-spring-boot-starter:3.34.0") // пример актуальной версии

	// 6) Prometheus / Micrometer
	implementation("io.micrometer:micrometer-registry-prometheus")

	// 7) Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// 8) (Опционально) Swagger / OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// 9) (Опционально) email
	// implementation("org.springframework.boot:spring-boot-starter-mail")

	// Тестирование
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
