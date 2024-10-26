package org.example.notioncodeexec.service

import reactor.core.publisher.Mono

interface ParagraphExecutionService {

    fun executeParagraph(code: String): Mono<String>
}