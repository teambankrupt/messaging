package com.example.messaging.domains.broadcast

import com.fasterxml.jackson.annotation.JsonProperty

interface WSMessage {
    val username: String?
    val message: String
    val data: Map<String, String>
}

data class PingMessage(
    override val username: String? = null,
    override val message: String,
    override val data: Map<String, String> = mapOf()
) : WSMessage

data class EnterMessage(
    override val username: String?,
    override val message: String,
    override val data: Map<String, String> = mapOf()
) : WSMessage



