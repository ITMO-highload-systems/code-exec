package org.example.notioncodeexec

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    companion object {
        // Контейнер для Python execution
        private val pythonContainer = GenericContainer(DockerImageName.parse("python:3.10-slim"))
            .apply {
                withCommand("tail", "-f", "/dev/null") // Оставляем контейнер "живым"
            }

        // Контейнер для PostgreSQL
        @ServiceConnection
        internal val postgres = PostgreSQLContainer("postgres:latest")
            .withInitScript("db/V1_init_schema.sql")

        @BeforeAll
        @JvmStatic
        fun setup() {
            // Запускаем оба контейнера
            pythonContainer.start()
            postgres.start()
        }

        @AfterAll
        @JvmStatic
        fun teardown() {
            pythonContainer.stop()
            postgres.stop()
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDockerProperties(registry: DynamicPropertyRegistry) {
            registry.add("application.docker.image") { pythonContainer.containerName }
        }
    }
}