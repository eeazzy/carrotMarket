package com.example.carrotmarket

import com.google.firebase.Timestamp

data class ChatData(
    val productName: Any,
    val uid: String,
    val uid_Name: String,
    val uid2: String,
    val uid2_Name: String,
    val message: Message
) {
    constructor() : this("", "","","", "", Message()) // 현재 시간으로 초기화
}


data class Message(
    val senderUid: String, // 발신자의 uid 프로그램 구동기준 current uid
    val content: String, // 메시지 내용
    val time:Timestamp
) {
    constructor() : this("", "", Timestamp.now())
}
