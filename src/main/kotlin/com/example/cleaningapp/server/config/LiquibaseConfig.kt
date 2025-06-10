package com.example.cleaningapp.server.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import liquibase.integration.spring.SpringLiquibase
import javax.sql.DataSource

@Configuration
class LiquibaseConfig(
    private val dataSource: DataSource,
    @Value("\${spring.liquibase.change-log}") private val changeLog: String
) {

    @Bean
    fun liquibase(): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = dataSource  // <- здесь DataSource не должен быть null
        liquibase.changeLog = "classpath:db/changelog/db.changelog-master.yaml"
        return liquibase
    }
}
