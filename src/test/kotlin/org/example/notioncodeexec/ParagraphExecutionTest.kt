package org.example.notioncodeexec

import org.example.notioncodeexec.controller.ParagraphExecutionController
import org.example.notioncodeexec.dto.ExecuteParagraphRequest
import org.example.notioncodeexec.repository.ExecutionCodeResultRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ParagraphExecutionTest : AbstractIntegrationTest() {

    @Autowired
    lateinit var executionCodeRepository: ExecutionCodeResultRepository

    @Autowired
    lateinit var paragraphExecutionController: ParagraphExecutionController

    private val paragraphId = 1L


    @Test
    fun `executeParagraph - simple code - correct answer`() {
        paragraphExecutionController.executeCode(ExecuteParagraphRequest(paragraphId, "print('Hello, World!')"))
        executionCodeRepository.findByParagraphId(paragraphId)?.let { result ->
            Assertions.assertEquals("Hello, World!\n", result.codeResult)  // Здесь проверяем сохраненное значение
        }
    }

    @Test
    fun `executeParagraph - incorrect code - correct answer with syntax error`() {
        paragraphExecutionController.executeCode(ExecuteParagraphRequest(paragraphId, "print('Hello, World!'"))
        executionCodeRepository.findByParagraphId(paragraphId)?.let { result ->
            Assertions.assertTrue(result.codeResult.contains("SyntaxError"))  // Здесь проверяем сохраненное значение
        }
    }
}