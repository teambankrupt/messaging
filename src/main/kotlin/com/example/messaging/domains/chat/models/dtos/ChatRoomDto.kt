package com.example.messaging.domains.chat.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class ChatRoomDto : BaseDto() {

    var title: String? = null

    @NotNull
    @NotEmpty
    lateinit var users: List<String>
}