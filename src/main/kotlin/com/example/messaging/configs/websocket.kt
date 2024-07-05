package com.example.messaging.configs

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

const val TOPIC_PREFIX = "/topic"
const val QUEUE_PREFIX = "/queue"
const val MESSAGES = "/messages"

@Configuration
@EnableWebSocketMessageBroker
open class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(TOPIC_PREFIX)
        registry.setApplicationDestinationPrefixes(MESSAGES)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        val allowedOrigins = arrayOf(
            "servicito.com", "dev.servicito.com", "servicito.com/*",
            "astha.app", "dev.astha.app", "astha.app/*",
            "cognitox.org", "dev.cognitox.org", "cognitox.org/*"
        )
        registry.addEndpoint("/connect").setAllowedOrigins(*allowedOrigins)
        registry.addEndpoint("/connect").setAllowedOrigins(*allowedOrigins).withSockJS()
    }

}

