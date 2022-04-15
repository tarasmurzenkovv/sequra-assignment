package com.sequra.sequraassignment.initializer

import org.springframework.boot.test.util.TestPropertyValues.of
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.DockerImageName.parse

class CustomDbContainer(dockerImageName: DockerImageName?) : PostgreSQLContainer<CustomDbContainer>(dockerImageName)

class PostgreInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val dbContainer = CustomDbContainer(parse("postgres").withTag("latest"))
        .withUsername("sa")
        .withPassword("sa")
        .withInitScript("migration/init_script.sql")

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        dbContainer.start()
        of(
            "spring.datasource.url=" + dbContainer.jdbcUrl,
            "spring.datasource.username=" + dbContainer.username,
            "spring.datasource.password=" + dbContainer.password
        ).applyTo(applicationContext.environment);
    }
}
