package com.example.carrotmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carrotmarket.R
import com.example.carrotmarket.UserDatastock

import com.example.carrotmarket.databinding.FragmentAddproductBinding
import com.example.carrotmarket.databinding.FragmentEditproductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class editProductFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var mBinding: FragmentEditproductBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        mBinding = FragmentEditproductBinding.inflate(inflater, container, false)
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        var sameUser :String=""
        var sellerName:String=""//판매자의 이름  save
        var sellerItemUid:String=""
        var switch:Switch?=null
        var sellStates:Boolean?=false
        var productTitle = arguments?.getString("productTitle")//productTitle 은 제품의 itemUid
        //Toast.makeText(requireContext(), productTitle, Toast.LENGTH_LONG).show()
        val docRef = firestore.collection("userDataStock").document(productTitle.toString())//데이터베이스에서 get
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    mBinding?.editTitle?.setText(document.getString("title"))
                    mBinding?.editPrice?.setText(document.getDouble("price").toString())
                    mBinding?.editDetails?.setText(document.getString("detail"))
                    sellerItemUid=document.getString("itemUid").toString()
                    sameUser=document.getString("uid").toString()
                    sellerName=document.getString("sellerName").toString()
                    sellStates=document.getBoolean("sell")//판매상태 저장
                    switch=mBinding?.sellState
                    if (switch != null) {
                        if(!sellStates!!) {
                            switch!!.isChecked = false
                        }
                        else{
                            switch!!.isChecked = true
                        }
                    }
                    mBinding?.sellState?.setOnCheckedChangeListener { buttonView, isChecked ->
                        if(isChecked){
                            switch!!.isChecked = true
                            sellStates=true
                        }
                        else{
                            switch!!.isChecked = false
                            sellStates=false
                        }
                    }

                }
            }
            .addOnFailureListener { exception ->
            }


        val backBtn=mBinding?.editBackHomeBtn
        val editItemBtn=mBinding?.editItem
        editItemBtn?.setOnClickListener {

            if (user != null) {
                if(sameUser == user.uid) {
                    var title = mBinding?.editTitle?.text.toString()
                    var detail = mBinding?.editDetails?.text.toString()
                    var price = mBinding?.editPrice?.text.toString().toDoubleOrNull()
                    if(price!=null) {
                        sellStates?.let { it1 ->
                            editUserToFirestore(sellerItemUid,sameUser,sellerName, title,detail, price,
                                it1
                            )
                        }//판매자의 이름은 그대로 저장
                        Toast.makeText(requireContext(), "수정완료", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(context, "유효한 가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

        backBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_editProductFragment2_to_homeFragment)//프래그먼트의 이동
            this.onDestroyView()
        }


        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    private fun editUserToFirestore(itemUid:String,uid: String,name:String ,title: String, detail: String, price: Double, isSell: Boolean) {
        val userData = UserDatastock(itemUid,uid,name,title, detail, price, isSell)
        // Cloud Firestore에 데이터 추가
        firestore.collection("userDataStock").document(itemUid).set(userData)
    }

    private fun deleteUserItemFromFirestore(title: String) {
        // Cloud Firestore에서 해당 UID를 가진 도큐먼트를 삭제
        firestore.collection("userDataStock").document(title).delete()//해당 uid에 해당하는 모든
    }

}