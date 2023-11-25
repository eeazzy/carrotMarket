package com.example.carrotmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.ChatAdapter
import com.example.carrotmarket.ChatData
import com.example.carrotmarket.Message
import com.example.carrotmarket.R

import com.example.carrotmarket.databinding.FragmentChattingBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.util.UUID

class chattingFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var mBinding: FragmentChattingBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val MessageData = ArrayList<Message>()
    private lateinit var uid_Name:String
    private lateinit var uid2_Name:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChattingBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        mBinding = binding
        recyclerView = binding.chatRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ChatAdapter(MessageData, currentUser?.uid.toString())
        recyclerView.adapter = adapter
        val getProductUid = arguments?.getString("getProductUid")

        val chatBtn= mBinding?.chatButton
        val exitChatBtn=mBinding?.exitChatBtn

        //채팅방 만들기
        var chatRoomId = UUID.randomUUID().toString()

        /*if (currentUser != null && getProductUid != null) {
        val sortedUids = listOf(currentUser.uid, getProductUid).sorted()
        sortedUids.joinToString("_") // 상대방의 UID를 기반으로 채팅 방 ID 생성
    } else {
        // 적절한 처리를 수행
    }*/

        val query = firestore.collection("chatRoom").document(chatRoomId.toString())
            .collection("messages")
            .orderBy("time")  // 메시지 시간 순으로 정렬
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

        //uid와 uid2의 이름을 가져오기 위한 부분
        val queryuid = firestore.collection("users").whereEqualTo("uid",currentUser?.uid.toString() )
        queryuid.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 검색된 문서에서 원하는 작업을 수행
                    uid_Name = document.getString("name").toString()
                }
            }
            .addOnFailureListener { exception ->
                println("문서 검색 중 오류 발생: $exception")
            }

        //uid2의 name을 가져오기 위한 부분
        val queryuid2 = firestore.collection("users").whereEqualTo("uid", getProductUid.toString())
        queryuid2.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // 검색된 문서에서 원하는 작업을 수행
                    uid2_Name = document.getString("name").toString()
                }
            }
            .addOnFailureListener { exception ->
                println("문서 검색 중 오류 발생: $exception")
            }




        chatBtn?.setOnClickListener {
            val setText = mBinding?.chatSendingText?.text.toString()

            if (setText.isNotEmpty()) {

                // 새로운 메시지 생성
                val newMessage = Message(currentUser?.uid?.toString() ?: "", setText, Timestamp.now())
                val newChatData = ChatData(chatRoomId.toString(), currentUser?.uid.toString(),uid_Name,getProductUid.toString() ,uid2_Name,newMessage)


                // Firestore에 채팅방 추가
                if (newChatData != null) {
                    firestore.collection("chatRoom").document(chatRoomId.toString())
                        .set(newChatData)
                        .addOnSuccessListener {
                            // 채팅방 추가 성공 시 할 일
                        }
                        .addOnFailureListener { exception ->
                            // 오류 처리
                        }
                }

                // Firestore에 메시지 추가
                // Firestore에 메시지 추가
                if (newChatData != null) {
                    firestore.collection("chatRoom").document(chatRoomId.toString())
                        .collection("messages")
                        .add(newMessage) // newMessage를 Firestore에 추가
                        .addOnSuccessListener {
                            // 메시지 추가 성공 시 할 일
                        }
                        .addOnFailureListener { exception ->
                            // 오류 처리
                        }
                }


                // 메시지 전송 후 입력 필드 초기화
                mBinding?.chatSendingText?.text = null
            }
        }




        exitChatBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_chattingFragment_to_homeFragment)//프래그먼트의 이동
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