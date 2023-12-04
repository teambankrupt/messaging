package com.example.messaging.domains.broadcast

import com.example.common.utils.TimeUtility
import com.example.messaging.routing.Route
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
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
        val headers = SimpMessageHeaderAccessor.wrap(message)

        headers.sessionId?.let {
            this.broadcastService.broadcast(
                Route.WS.userQueue(sessionId = it),
                message.payload
            )
        }

    }

    @Scheduled(fixedRate = 5000)
    fun broadcastTime() {
        this.broadcastService.broadcast(Route.WS.TOPIC_TIME, object : WSMessage() {
            override val message: String = "Time: ${TimeUtility.readableTimeFromInstant(Instant.now())}"
        })
    }

}

data class PingMessage(override val message: String) : WSMessage()
data class Item(
    val message: String,
    val username: String,
    val data: Map<String, Any>
)
abstract class WSMessage {
    val username: String? = null
    abstract val message: String
    val data: Map<String, Any> = mapOf()
}

@Component
class BroadcastService(
    private val simpMessagingTemplate: SimpMessagingTemplate
) {

    fun <T : WSMessage> broadcast(topic: String, message: T) =
        this.simpMessagingTemplate.convertAndSend(topic, message)

//    fun <T: WSMessage>broadcastToUser(queue: Route.WS.V1.Queues, message: T) =
//        this.simpMessagingTemplate.convertAndSendToUser(queue.value, message)


}