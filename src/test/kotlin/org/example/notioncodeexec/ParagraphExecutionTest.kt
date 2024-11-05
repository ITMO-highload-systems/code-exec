package org.example.notioncodeexec

import org.example.notioncodeexec.auth.JwtUtil
import org.example.notioncodeexec.repository.ExecutionCodeResultRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION

class ParagraphExecutionTest : AbstractIntegrationTest() {

    @Autowired
    lateinit var executionCodeRepository: ExecutionCodeResultRepository

    @Autowired
    lateinit var jwtUtil: JwtUtil

    private val paragraphId = 1L

    @AfterEach
    fun cleanUp() {
        executionCodeRepository.deleteAll()
    }


    @Test
    fun `executeParagraph - simple code - correct answer`() {
        webTestClient.get()
            .uri("/api/v1/execution/execute?paragraphId=$paragraphId&code=print('Hello, World!')")
            .header(AUTHORIZATION, "Bearer " + jwtUtil.generateServerToken())
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .consumeWith { response ->
                val responseBody = response.responseBody
                Assertions.assertEquals("Hello, World!\n", responseBody)  // Здесь проверяем ожидаемое значение
            }

        executionCodeRepository.findByParagraphId(paragraphId)?.let { result ->
            Assertions.assertEquals("Hello, World!\n", result.codeResult)  // Здесь проверяем сохраненное значение
        }
    }

    @Test
    fun `executeParagraph - incorrect code - correct answer with syntax error`() {
        webTestClient.get()
            .uri("/api/v1/execution/execute?paragraphId=$paragraphId&code=print('Hello, World!'")
            .header(AUTHORIZATION, "Bearer " + jwtUtil.generateServerToken())
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