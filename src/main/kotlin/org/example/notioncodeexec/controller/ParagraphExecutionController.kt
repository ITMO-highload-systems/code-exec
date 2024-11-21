package org.example.notioncodeexec.controller

import org.example.notioncodeexec.dto.ExecuteParagraphRequest
import org.example.notioncodeexec.service.ParagraphExecutionService
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.RestController

@RestController
class ParagraphExecutionController(
    private val paragraphExecutionService: ParagraphExecutionService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ParagraphExecutionController::class.java)
    }

    @MessageMapping("/paragraph.execute")
    fun sendMessage(
        @Payload request: ExecuteParagraphRequest
    ) {
        logger.info("Received message: $request")
        paragraphExecutionService.executeParagraph(request)
            .doOnSuccess { logger.info("Execution completed") }
            .doOnError { logger.error("Error while executing code: ${it.message}") }
            .subscribe()
    }
}