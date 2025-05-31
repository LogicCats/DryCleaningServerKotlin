package com.example.cleaningapp.server.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import liquibase.integration.spring.SpringLiquibase
import javax.sql.DataSource

@Configuration
class LiquibaseConfig(
    private val dataSource: DataSource,
    @Value("\${spring.liquibase.change-log}") private val changeLog: String
) {

    @Bean
    fun liquibase(): SpringLiquibase {
        return SpringLiquibase().apply {
            this.dataSource = dataSource
            this.changeLog = changeLog
            // Можно задать другие параметры, например:
            // this.contexts = "development, production"
            // this.defaultSchema = "public"
        }
    }
}
