package com.example.carrotmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.ChatData
import com.example.carrotmarket.MyChatRoomAdapter
import com.example.carrotmarket.databinding.FragmentUserdataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class UserdataFragment : Fragment() {
    private var mBinding: FragmentUserdataBinding? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyChatRoomAdapter
    private val ChatDatass = ArrayList<ChatData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        firestore = FirebaseFirestore.getInstance()
        val binding = FragmentUserdataBinding.inflate(inflater, container, false)
        mBinding = binding
        recyclerView = binding.userRecycle
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyChatRoomAdapter(ChatDatass)

        adapter.setItemClickListener(object : MyChatRoomAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                //행동할 것
                if (ChatDatass[position].uid2 == user?.uid||ChatDatass[position].uid== user?.uid) {
                    val getChatRoomId = ChatDatass[position].productName.toString() // 채팅방 ID를 가져옵니다
                    val action = UserdataFragmentDirections.actionUserdataFragmentToOtherChattingFragment(getChatRoomId)
                    findNavController().navigate(action)
                }
            }
        })


        user?.uid?.let { currentUserId ->
            val query1 = firestore.collection("chatRoom")
                .whereEqualTo("uid", currentUserId)

            val query2 = firestore.collection("chatRoom")
                .whereEqualTo("uid2", currentUserId)

            query1.get()
                .addOnSuccessListener { querySnapshot1 ->
                    query2.get()
                        .addOnSuccessListener { querySnapshot2 ->
                            // querySnapshot1과 querySnapshot2를 합쳐서 처리
                            val combinedSnapshot = mutableListOf<QueryDocumentSnapshot>()
                            combinedSnapshot.addAll(querySnapshot1)
                            combinedSnapshot.addAll(querySnapshot2)

                            // 합쳐진 데이터를 처리
                            addChatRoomsToList(combinedSnapshot)
                        }
                        .addOnFailureListener { exception ->
                            // query2 실패 처리
                        }
                }
                .addOnFailureListener { exception ->
                    // query1 실패 처리
                }
        }






        recyclerView.adapter = adapter
        return mBinding?.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
    fun addChatRoomsToList(querySnapshot: MutableList<QueryDocumentSnapshot>) {
        val dataList = mutableListOf<ChatData>()

        for (document in querySnapshot) {
            val chatDataRoom = document.toObject(ChatData::class.java)
            dataList.add(chatDataRoom)
        }

        if (dataList.isNotEmpty()) {
            ChatDatass.clear()
            ChatDatass.addAll(dataList)
            adapter.notifyDataSetChanged()
        }
    }
}
