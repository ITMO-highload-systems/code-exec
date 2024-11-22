package org.example.notion.kafka

data class Message(
    override val type: Type,
    val message: String,
    val noteId: Long,
    val paragraphId: Long
) : AbstractMessage(type)