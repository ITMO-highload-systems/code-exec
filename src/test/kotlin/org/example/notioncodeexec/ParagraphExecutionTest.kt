package org.example.notioncodeexec

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParagraphExecutionTest : AbstractIntegrationTest() {

    @Test
    fun `executeParagraph - simple code - correct answer`() {
        webTestClient.get()
            .uri("/api/v1/execution/execute?code=print('Hello, World!')")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .consumeWith { response ->
                val responseBody = response.responseBody
                Assertions.assertEquals("Hello, World!\n", responseBody)  // Здесь проверяем ожидаемое значение
            }
    }

    @Test
    fun `executeParagraph - incorrect code - correct answer with syntax error`() {
        webTestClient.get()
            .uri("/api/v1/execution/execute?code=print('Hello, World!'")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .consumeWith { response ->
                val responseBody = response.responseBody
                Assertions.assertTrue(
                    responseBody?.contains("SyntaxError") == true,
                    "Expected SyntaxError message, but got: $responseBody"
                )
            }
    }
}