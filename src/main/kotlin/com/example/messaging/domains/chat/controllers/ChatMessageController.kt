package com.example.messaging.domains.chat.controllers

import com.example.common.utils.ExceptionUtil
import com.example.coreweb.commons.Constants
import com.example.messaging.domains.chat.models.dtos.ChatMessageDto
import com.example.messaging.domains.chat.models.mappers.ChatMessageMapper
import com.example.messaging.domains.chat.services.ChatMessageService
import com.example.messaging.routing.Route
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.validation.Valid

@RestController
@Api(tags = [Constants.Swagger.CHAT_MESSAGES])
class ChatMessageController @Autowired constructor(
    private val chatMessageMapper: ChatMessageMapper,
    private val chatMessageService: ChatMessageService,
    private val simpMessagingTemplate: SimpMessagingTemplate
) {

    @MessageMapping(Route.V1.CHAT)
//    @SendTo("/topic/messages")
    fun send(@Valid @Payload chatMessageDto: ChatMessageDto) {
        if (chatMessageDto.from == null) // TODO: remove this check when security implementatiion is done
            throw ExceptionUtil.invalid("`from` can't be null")
        var message = this.chatMessageMapper.map(chatMessageDto, null)
        message = this.chatMessageService.save(message)
        val dto = this.chatMessageMapper.map(message)
        return this.simpMessagingTemplate.convertAndSend("/topic/chatrooms/${chatMessageDto.chatRoomId}/messages", dto)
    }

    class OutputMesssage(private val from: String, private val text: String, private val time: String)
}
