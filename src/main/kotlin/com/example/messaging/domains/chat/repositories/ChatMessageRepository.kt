package com.example.messaging.domains.chat.repositories


import com.example.messaging.domains.chat.models.entities.ChatMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm WHERE (:q IS NULL OR cm.content LIKE %:q%) AND cm.deleted=false")
    fun search(@Param("q") query: String?, pageable: Pageable): Page<ChatMessage>

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.id=:id AND cm.deleted=false")
    fun find(@Param("id") id: Long): Optional<ChatMessage>

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id=:chatroomId" +
            " AND (:lastMessageId IS NULL OR cm.id>:lastMessageId) AND cm.deleted=false")
    fun findForChatroom(@Param("chatroomId") id: Long, @Param("lastMessageId") lastMessageId: Long?): List<ChatMessage>

}