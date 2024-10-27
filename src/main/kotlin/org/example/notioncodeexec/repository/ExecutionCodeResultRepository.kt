package org.example.notioncodeexec.repository

import org.example.notioncodeexec.model.ExecutionCodeResult
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface ExecutionCodeResultRepository: CrudRepository<ExecutionCodeResult, Long> {

    @Query("SELECT * FROM execution_code_result WHERE paragraph_id = :paragraphId")
    fun findByParagraphId(paragraphId: Long): ExecutionCodeResult?
}