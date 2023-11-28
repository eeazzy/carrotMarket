package com.example.carrotmarket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyChatRoomAdapter (val items: List<ChatData>) : RecyclerView.Adapter<MyChatRoomAdapter.ChatRoomViewHolder>() {

    class ChatRoomViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val roomNam = v.findViewById<TextView>(R.id.RoomName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.showroom, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val roomDatas = items[position]
        holder.roomNam.text = roomDatas.uid_Name.toString()+"과 "+roomDatas.uid2_Name.toString()+"의 채팅방"
        holder.roomNam.setOnClickListener {
            itemClickListener.onClick(it, position)

        }
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener
}