package org.example.notioncodeexec.service.impl

import org.example.notioncodeexec.config.DockerConfig
import org.example.notioncodeexec.model.ExecutionCodeResult
import org.example.notioncodeexec.service.ExecutionResultService
import org.example.notioncodeexec.service.ParagraphExecutionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class ParagraphExecutionServiceImpl(
    val dockerConfig: DockerConfig,
    val executionResultService: ExecutionResultService
): ParagraphExecutionService {

    companion object {
        private val logger = LoggerFactory.getLogger(ParagraphExecutionServiceImpl::class.java)
        private const val CODE_EXECUTION_ERROR = "Error while execution code: %s"
        private const val EXECUTING_CODE_INFO = "Executing code: %s"
    }

    override fun executeParagraph(paragraphId: Long, code: String): Mono<String> {
        return Mono.fromCallable {
            try {
                logger.info(EXECUTING_CODE_INFO.format(code))
                val processBuilder = ProcessBuilder("docker", "exec", dockerConfig.image, "python3", "-c", code)
                processBuilder.redirectErrorStream(true)

                val process = processBuilder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val output = reader.readText()
                process.waitFor()
                executionResultService.saveExecutionResult(ExecutionCodeResult(paragraphId, output))
                output
            } catch (ex: Exception) {
                logger.error(CODE_EXECUTION_ERROR.format(ex.message))
                CODE_EXECUTION_ERROR.format(ex.message)
            }
        }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnError { e -> logger.error(CODE_EXECUTION_ERROR.format(e.message)) }
    }
}