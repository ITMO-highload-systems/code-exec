package org.example.notioncodeexec.dto

data class ExecuteParagraphRequest(
    val paragraphid: Long,
    val noteId: Long,
    val code: String
)
