package com.example.messaging.domains.broadcast

import com.example.common.utils.TimeUtility
import com.example.messaging.configs.QUEUE_PREFIX
import com.example.messaging.configs.TOPIC_PREFIX
import com.example.messaging.routing.Route
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


@RestController
class BroadcastController(
    private val broadcastService: BroadcastService
) {

    @MessageMapping(Route.WS.PING)
    fun ping(@Payload message: Message<PingMessage>) {

        this.broadcastService.broadcast(Route.WS.TOPIC_PING, message.payload)

        /*
        will be implemented correctly when session mapping is implemented.
         */
//        val headers = SimpMessageHeaderAccessor.wrap(message)
//        headers.sessionId?.let { sessionId ->
//            this.broadcastService.broadcastToUser(
//                sessionId,
//                Route.WS.QUEUE_PING,
//                message.payload
//            )
//        }

    }

    @Scheduled(fixedRate = 5000)
    fun broadcastTime() {
        this.broadcastService.broadcast(
            Route.WS.TOPIC_TIME, PingMessage(
                message = "Time: ${TimeUtility.readableTimeFromInstant(Instant.now())}"
            )
        )
    }

}

@Component
class BroadcastService(
    private val simpMessagingTemplate: SimpMessagingTemplate
) {

    fun <T : WSMessage> broadcast(topic: String, message: T) =
        this.simpMessagingTemplate.convertAndSend("$TOPIC_PREFIX$topic", message)

    fun <T : WSMessage> broadcastToUser(sessionId: String, queue: String, message: T) =
        this.simpMessagingTemplate.convertAndSendToUser(sessionId, "$QUEUE_PREFIX$queue", message)

}