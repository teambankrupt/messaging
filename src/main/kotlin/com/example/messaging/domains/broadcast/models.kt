package com.example.messaging.domains.broadcast

import com.fasterxml.jackson.annotation.JsonProperty

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

data class VotedMessage(
    override val username: String?,
    override val message: String,
    override val data: Map<String, Any> = mapOf(),
    @JsonProperty("user_voted_options")
    val userVotedOptions: Set<String> = setOf()
) : WSMessage


