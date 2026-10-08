package com.friends.chat

import com.google.firebase.Timestamp

data class Message(
    val text: String = "",
    val senderUid: String = "",
    val senderName: String = "",
    val time: Timestamp? = null
)
