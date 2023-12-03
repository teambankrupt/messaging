package com.example.messaging.domains.chat.controllers

import com.example.auth.config.security.SecurityContext
import com.example.coreweb.commons.Constants
import com.example.messaging.domains.chat.models.dtos.ChatMessageDto
import com.example.messaging.domains.chat.models.mappers.ChatMessageMapper
import com.example.messaging.domains.chat.services.ChatMessageService
import com.example.messaging.routing.Route
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Api(tags = [Constants.Swagger.CHAT_MESSAGES])
class ChatHttpApiController @Autowired constructor(
    private val chatMessageService: ChatMessageService,
    private val chatMessageMapper: ChatMessageMapper
) {

    @GetMapping(Route.V1.FIND_CHATROOM_MESSAGES)
    fun findChatroomMessages(
        @PathVariable("id") chatroomId: Long,
        @RequestParam("last_message_id", required = false) lastMessageId: Long?
    ): ResponseEntity<List<ChatMessageDto>> {
        val messages = this.chatMessageService.findForChatroom(chatroomId, lastMessageId)
        return ResponseEntity.ok(messages.map { this.chatMessageMapper.map(it) })
    }

    @PostMapping(Route.V1.CHAT_SEND)
    fun send(@Valid @RequestBody chatMessageDto: ChatMessageDto): ResponseEntity<ChatMessageDto> {
        val auth = SecurityContext.getCurrentUser()
        chatMessageDto.from = auth.username
        var message = this.chatMessageMapper.map(chatMessageDto, null)
        message = this.chatMessageService.save(message)
        return ResponseEntity.ok(this.chatMessageMapper.map(message))
    }

}