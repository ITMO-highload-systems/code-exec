package org.example.notioncodeexec.service.impl

import org.example.notioncodeexec.config.DockerConfig
import org.example.notioncodeexec.service.ParagraphExecutionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class ParagraphExecutionServiceImpl(val dockerConfig: DockerConfig): ParagraphExecutionService {

    companion object {
        private val logger = LoggerFactory.getLogger(ParagraphExecutionServiceImpl::class.java)
        private const val CODE_EXECUTION_ERROR = "Error while execution code: %s"
    }

    override fun executeParagraph(code: String): Mono<String> {
        return Mono.fromCallable {
            try {
                val processBuilder = ProcessBuilder("docker", "exec", dockerConfig.image, "python3", "-c", code)
                processBuilder.redirectErrorStream(true)

                val process = processBuilder.start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val output = reader.readText()
                process.waitFor()
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