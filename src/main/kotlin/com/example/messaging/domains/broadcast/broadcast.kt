package com.example.messaging.domains.broadcast

import com.example.common.utils.TimeUtility
import com.example.messaging.configs.QUEUE_PREFIX
import com.example.messaging.configs.TOPIC_PREFIX
import com.example.messaging.routing.Route
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
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

    @Scheduled(fixedRate = 60000)
    fun broadcastTime() {
        this.broadcastService.broadcast(
            Route.WS.TOPIC_TIME, PingMessage(
                message = "Time: ${TimeUtility.readableTimeFromInstant(Instant.now())}"
            )
        )
    }

}

@Service
class BroadcastService(
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    private val logger = LoggerFactory.getLogger(BroadcastService::class.java)

    fun <T : WSMessage> broadcast(topic: String, message: T) = try {
        this.simpMessagingTemplate.convertAndSend("$TOPIC_PREFIX$topic", message)
    } catch (e: Exception) {
        logger.error("Error occurred when sending broadcast to topic: $topic")
        logger.error(e.toString())
    }


    fun <T : WSMessage> broadcastToUser(sessionId: String, queue: String, message: T) = try {
        this.simpMessagingTemplate.convertAndSendToUser(sessionId, "$QUEUE_PREFIX$queue", message)
    } catch (e: Exception) {
        logger.error("Error occurred when sending broadcast to user/session id: $sessionId")
        logger.error(e.toString())
    }


}