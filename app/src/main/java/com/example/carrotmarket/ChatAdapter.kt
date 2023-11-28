package com.example.carrotmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(val items: List<Message>, val currentUid: String) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(v: View, val messageText: TextView) : RecyclerView.ViewHolder(v)

    override fun getItemViewType(position: Int): Int {
        return if (items[position].senderUid == currentUid){
            R.layout.showchat_right
        } else {
            R.layout.showchat_left
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)

        val textViewId = if (viewType == R.layout.showchat_right){
            R.id.chatText2
        } else {
            R.id.chatText
        }
        val messageText = view.findViewById<TextView>(textViewId)

        return ChatViewHolder(view, messageText)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = items[position]

        holder.messageText.text = message.content

    }
}