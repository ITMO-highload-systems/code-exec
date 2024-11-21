package org.example.notioncodeexec.service

import org.example.notioncodeexec.dto.ExecuteParagraphRequest
import reactor.core.publisher.Mono

interface ParagraphExecutionService {

    fun executeParagraph(executionParagraphRequest: ExecuteParagraphRequest): Mono<Void>
}