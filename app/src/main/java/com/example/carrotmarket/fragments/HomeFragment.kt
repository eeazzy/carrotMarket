package com.example.carrotmarket.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.LoginActivity
import com.example.carrotmarket.MyAdapter
import com.example.carrotmarket.R
import com.example.carrotmarket.UserDatastock
import com.example.carrotmarket.databinding.FragmentHomeBinding
import com.example.carrotmarket.databinding.FragmentUserdataBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var mBinding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private val userList = ArrayList<UserDatastock>()
    private var flag: Boolean = false//가격 상한 체크
    private var sellFlag: Boolean = false//판매여부 체크

    private var count: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firestore = FirebaseFirestore.getInstance()
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        mBinding = binding
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        recyclerView = binding.myRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MyAdapter(userList)
        val sortByIgnore: Button? = binding.ignoreSellingProduct

        adapter.setItemClickListener(object: MyAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                if(userList[position].uid==user?.uid) {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToEditProductFragment2(userList[position].itemUid)
                    findNavController().navigate(action)
                }
                else{
                    Toast.makeText(requireContext(), "다른 사용자의 제품", Toast.LENGTH_LONG).show()
                    val action2 =
                        HomeFragmentDirections.actionHomeFragmentToShowOtherProductFragment(userList[position].itemUid)
                    findNavController().navigate(action2)
                }
            }
        })




        recyclerView.adapter = adapter

        val sortByHighest: Button? = binding.highestPrice
        val sortByLowest: Button? = binding.lowestPrice
        val addProdcutBtn:FloatingActionButton? = binding.addProductBtn
        val logOutBtn: Button? = binding.logoutBtn


        fun updateData() {
            val query = if (!sellFlag) {
                firestore.collection("userDataStock")
                    .whereEqualTo("sell", false)
                    .limit(30)
            } else {
                firestore.collection("userDataStock")
                    .orderBy("price")
                    .limit(30)
            }
            query.get()
                .addOnSuccessListener { querySnapshot ->
                    val dataList = mutableListOf<UserDatastock>()

                    for (document in querySnapshot) {
                        val userData = document.toObject(UserDatastock::class.java)
                        dataList.add(userData)
                    }

                    if (flag) {
                        dataList.sortBy { it.price }
                    } else {
                        dataList.sortByDescending { it.price }
                    }

                    userList.clear()
                    userList.addAll(dataList)
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    // 에러 처리
                }
        }

        updateData() // 초기 데이터 로드

        sortByHighest?.setOnClickListener {
            flag = false
            updateData()
        }

        sortByLowest?.setOnClickListener {
            flag = true
            updateData()
        }

        sortByIgnore?.setOnClickListener {
            count++
            sellFlag = count % 2 == 1
            updateData()
        }

        addProdcutBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addProductFragment2)
        }

        logOutBtn?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()//로그아웃
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        return mBinding?.root
    }




    override fun onDestroyView() {//바인딩 정책에 의거,해당 view를 destroy
        mBinding = null
        super.onDestroyView()
    }
}

