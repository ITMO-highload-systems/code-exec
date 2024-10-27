package org.example.notioncodeexec.service

import reactor.core.publisher.Mono

interface ParagraphExecutionService {

    fun executeParagraph(paragraphId: Long, code: String): Mono<String>
}