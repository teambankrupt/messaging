package com.example.messaging.domains.broadcast

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class BroadcastController(
    private val simpMessagingTemplate: SimpMessagingTemplate
) {

    @Scheduled(fixedRate = 5000)
    fun broadcastTime() {
        this.simpMessagingTemplate.convertAndSend(
            "/topic/time",
            "Server: the time now is: ${Instant.now()}"
        )
    }

}