package com.example.messaging.domains.broadcast
interface WSMessage {
    val username: String?
    val message: String
    val data: Map<String, Any>
}
data class PingMessage(
    override val username: String? = null,
    override val message: String,
    override val data: Map<String, Any> = mapOf()
) : WSMessage

data class EnterMessage(
    override val username: String?,
    override val message: String,
    override val data: Map<String, Any> = mapOf()
) : WSMessage


