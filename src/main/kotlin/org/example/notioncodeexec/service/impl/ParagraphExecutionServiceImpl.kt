package org.example.notioncodeexec.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.notion.kafka.Message
import org.example.notion.kafka.Type
import org.example.notioncodeexec.config.DockerConfig
import org.example.notioncodeexec.dto.ExecuteParagraphRequest
import org.example.notioncodeexec.model.ExecutionCodeResult
import org.example.notioncodeexec.service.ExecutionResultService
import org.example.notioncodeexec.service.ParagraphExecutionService
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class ParagraphExecutionServiceImpl(
    val dockerConfig: DockerConfig,
    val executionResultService: ExecutionResultService,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
): ParagraphExecutionService {

    companion object {
        private val logger = LoggerFactory.getLogger(ParagraphExecutionServiceImpl::class.java)
        private const val CODE_EXECUTION_ERROR = "Error while execution code: %s"
        private const val EXECUTING_CODE_INFO = "Executing code: %s"
    }

    override fun executeParagraph(executionParagraphRequest: ExecuteParagraphRequest): Mono<Void> {
        return Mono.fromCallable {
            try {
                logger.info(EXECUTING_CODE_INFO.format(executionParagraphRequest.code))
                val processBuilder = ProcessBuilder(
                    "docker", "exec", dockerConfig.image,
                    "python3", "-c", executionParagraphRequest.code
                )
                processBuilder.redirectErrorStream(true)

                val process = processBuilder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val output = reader.readText()
                process.waitFor()

                // Сохранение результата выполнения
                executionResultService.saveExecutionResult(
                    ExecutionCodeResult(executionParagraphRequest.paragraphid, output)
                )

                // todo fix note id Вызов функции отправки уведомления
                sendResponse(
                    Message(
                        Type.PARAGRAPH_EXECUTED,
                        output,
                        1L,
                        executionParagraphRequest.paragraphid
                    )
                )
            } catch (ex: Exception) {
                logger.error(CODE_EXECUTION_ERROR.format(ex.message))
                CODE_EXECUTION_ERROR.format(ex.message)
            }
        }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnError { e -> logger.error(CODE_EXECUTION_ERROR.format(e.message)) }
            .then()
    }

    fun sendResponse(payload: Message) {
        val send = kafkaTemplate.send(
            "events",
            objectMapper.writeValueAsString(payload)
        )
        send.whenComplete { result, ex ->
            if (ex == null) {
                logger.info(
                    "Sent message=[$payload] with offset=[${result.recordMetadata.offset()})]"
                )
            } else {
                logger.error(
                    "Unable to send message=[$payload] due to :  ${ex.message}"
                )
            }
        }
    }
}