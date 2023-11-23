package com.example.carrotmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OtherChatAdapter(val items: List<Message>) : RecyclerView.Adapter<OtherChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val messageText = v.findViewById<TextView>(R.id.chatText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.showchat_left, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = items[position]
        holder.messageText.text = message.content.toString()
    }
}
