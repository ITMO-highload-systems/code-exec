package org.example.notioncodeexec

import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@AutoConfigureMockMvc
@SpringBootTest
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    companion object {
        private val genericContainer = GenericContainer(DockerImageName.parse("python:3.10-slim"))
            .withCommand("tail", "-f", "/dev/null")

        @BeforeAll
        @JvmStatic
        fun setup() {
            genericContainer.start()
            postgres.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDockerProperties(registry: DynamicPropertyRegistry) {
            registry.add("docker.image") { genericContainer.containerName }
        }

        @ServiceConnection
        internal var postgres = PostgreSQLContainer("postgres:latest")
            .withInitScript("db/test_data.sql")
    }
}