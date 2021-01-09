package com.example.messaging.domains.chat.models.entities

import com.example.auth.config.security.SecurityContext
import com.example.common.misc.Commons
import com.example.coreweb.domains.base.entities.BaseEntity
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

@Entity
@Table(name = "chat_chatrooms", schema = "messaging")
class ChatRoom : BaseEntity() {

    var title: String? = null
        get() {
            return if (field.isNullOrBlank()) {
                if (users.size == 2) users.first { it != SecurityContext.getLoggedInUsername() } else Commons.summary(users.joinToString(), 50)
            } else field
        }

    @ElementCollection
    @JoinTable(name = "chat_chatroom_users", schema = "messaging")
    @LazyCollection(LazyCollectionOption.FALSE)
    lateinit var users: MutableList<String>
}
