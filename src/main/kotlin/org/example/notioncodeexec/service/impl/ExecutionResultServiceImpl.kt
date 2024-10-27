package org.example.notioncodeexec.service.impl

import org.example.notioncodeexec.exception.ExecutionResultNotFoundException
import org.example.notioncodeexec.model.ExecutionCodeResult
import org.example.notioncodeexec.repository.ExecutionCodeResultRepository
import org.example.notioncodeexec.service.ExecutionResultService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ExecutionResultServiceImpl (
    private val executionCodeResultRepository: ExecutionCodeResultRepository
): ExecutionResultService {

    companion object {
        private const val EXECUTION_CODE_RESULT_NOT_FOUND = "Execution code result not found"
    }

    override fun saveExecutionResult(executionCodeResult: ExecutionCodeResult): Mono<Unit> {
        return Mono.fromCallable { executionCodeResultRepository.save(executionCodeResult) }
    }

    override fun getExecutionCodeResult(paragraphId: Long): Mono<ExecutionCodeResult> {
        return Mono.fromCallable { executionCodeResultRepository.findByParagraphId(paragraphId) }
            .flatMap { result ->
                if (result != null) {
                    Mono.just(result)
                } else {
                    Mono.error(ExecutionResultNotFoundException(EXECUTION_CODE_RESULT_NOT_FOUND))
                }
            }
    }
}