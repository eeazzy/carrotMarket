package com.example.carrotmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.ChatAdapter
import com.example.carrotmarket.ChatData
import com.example.carrotmarket.Message
import com.example.carrotmarket.MyChatRoomAdapter
import com.example.carrotmarket.OtherChatAdapter
import com.example.carrotmarket.R
import com.example.carrotmarket.databinding.FragmentChattingBinding
import com.example.carrotmarket.databinding.FragmentOtherchattingBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.checkerframework.checker.units.qual.C

class OtherChattingFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var mBinding: FragmentOtherchattingBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OtherChatAdapter
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val MessageData = ArrayList<Message>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOtherchattingBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        mBinding = binding
        recyclerView = binding.otherChatRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = OtherChatAdapter(MessageData)
        recyclerView.adapter = adapter

        val chatRoomId = arguments?.getString("getChatRoomId")
        Toast.makeText(context, chatRoomId, Toast.LENGTH_LONG).show()
        val chatBtn = mBinding?.otherChatButton
        val exitChatBtn = mBinding?.otherExitChatBtn

        // Firestore에서 해당 채팅방의 메시지를 읽어오는 쿼리
        val query = firestore.collection("chatRoom").document(chatRoomId.toString())
            .collection("messages")
            .orderBy("time")
            .limit(100)

        query.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // 오류 처리
                return@addSnapshotListener
            }

            val dataList = mutableListOf<Message>()
            if (querySnapshot != null) {
                for (document in querySnapshot) {
                    val chatData = document.toObject(Message::class.java)
                    dataList.add(chatData)
                }
            }

            MessageData.clear()
            MessageData.addAll(dataList)
            adapter.notifyDataSetChanged()
        }

        chatBtn?.setOnClickListener {
            val setText = mBinding?.otherChatSendingText?.text.toString()

            if (setText.isNotEmpty()) {
                // 새로운 메시지 생성
                val newMessage = Message(
                    senderUid = currentUser?.uid ?: "",
                    content = setText,
                    time = Timestamp.now()
                )

                // Firestore에 메시지 추가
                firestore.collection("chatRoom").document(chatRoomId.toString())
                    .collection("messages")
                    .add(newMessage)
                    .addOnSuccessListener {
                        // 메시지 추가 성공 시 할 일
                    }
                    .addOnFailureListener { exception ->
                        // 오류 처리
                    }

                // 메시지 전송 후 입력 필드 초기화
                mBinding?.otherChatSendingText?.text = null
            }
        }

        exitChatBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_otherChattingFragment_to_homeFragment)
        }

        return mBinding?.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}

