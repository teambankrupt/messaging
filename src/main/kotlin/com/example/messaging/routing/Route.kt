package com.example.messaging.routing


class Route {
    class V1 {
        companion object {
            private const val API = "/api"
            private const val VERSION = "/v1"
            private const val ADMIN = "/admin"

            /**
             * WEBSOCKET - CHAT
             */
            // Chat Rooms
            const val SEARCH_CHATROOMS = "$API$VERSION$ADMIN/chat/chatrooms"
            const val SEARCH_MY_CHATROOMS = "$API$VERSION/chat/my-chatrooms"
            const val CREATE_CHATROOM = "$API$VERSION/chat/chatrooms"
            const val FIND_CHATROOM = "$API$VERSION/chat/chatrooms/{id}"
            const val UPDATE_CHATROOM = "$API$VERSION/chat/chatrooms/{id}"
            const val DELETE_CHATROOM = "$API$VERSION/chat/chatrooms/{id}"

            const val FIND_CHATROOM_MESSAGES = "$API$VERSION/chat/chatrooms/{id}/messages"
            const val CHAT_SEND = "$API$VERSION/chat/chatrooms/{id}/send"

            // Chat
            const val CHAT = "/chat"
        }

    }

    object WS {
        const val PING = "/ping"

        // TOPICS
        const val TOPIC_ALL = "/all"
        const val TOPIC_TIME = "/time"
        const val TOPIC_PING = PING

        // QUEUE
        const val QUEUE_PING = PING
    }
}