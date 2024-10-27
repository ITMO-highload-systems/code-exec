package org.example.notioncodeexec.service

import org.example.notioncodeexec.model.ExecutionCodeResult
import reactor.core.publisher.Mono

interface ExecutionResultService {

    fun saveExecutionResult(executionCodeResult: ExecutionCodeResult): Mono<Unit>

    fun getExecutionCodeResult(paragraphId: Long): Mono<ExecutionCodeResult>
}