package org.example.notioncodeexec.controller

import org.example.notioncodeexec.service.ParagraphExecutionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/execution")
class ParagraphExecutionController(
    private val paragraphExecutionService: ParagraphExecutionService
) {

    @GetMapping("/execute")
    fun executeParagraph(@RequestParam code: String): Mono<ResponseEntity<String>> {
        return paragraphExecutionService.executeParagraph(code)
            .map { result -> ResponseEntity.ok(result) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }
}