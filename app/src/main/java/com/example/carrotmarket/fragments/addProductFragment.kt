package com.example.carrotmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carrotmarket.R
import com.example.carrotmarket.UserDatastock

import com.example.carrotmarket.databinding.FragmentAddproductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class addProductFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var mBinding: FragmentAddproductBinding? = null
    private var myName:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        mBinding = FragmentAddproductBinding.inflate(inflater, container, false)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val name=firestore.collection("users").document(user?.uid.toString())

        name.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    myName= document.getString("name").toString()//name필드의 값을 가져와 myName에 넣어줘요
                } else {
                }
            }
            .addOnFailureListener { exception ->
            }

        var setTitle=mBinding?.setTitle?.text.toString()
        var setDetail=mBinding?.setDetails?.text.toString()
        var setPrice=mBinding?.setPrice?.text.toString()
        val backBtn=mBinding?.backHomeBtn//상품 등록에서 다시 뒤 fragment로 가는 코드
        val addProductBtn=mBinding?.setItem
        addProductBtn?.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            if (user != null) {
                val uid = user.uid
                val uuid = UUID.randomUUID().toString()
                var title=mBinding?.setTitle?.text.toString()
                var detail=mBinding?.setDetails?.text.toString()
                var price=mBinding?.setPrice?.text.toString().toDoubleOrNull()
                if(price!=null) {
                    addUserToFirestore(uuid, uid, myName, title, detail, price, false)
                    Toast.makeText(context, "데이터 저장 완료.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "유효한 가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "사용자가 로그인되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        backBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_addProductFragment2_to_homeFragment)//프래그먼트의 이동
            this.onDestroyView()
        }


        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    private fun addUserToFirestore(itemUid:String,uid: String,myName:String ,title: String, detail: String, price: Double, isSell: Boolean) {
        val userData = UserDatastock(itemUid,uid,myName, title, detail, price, isSell)
        // Cloud Firestore에 데이터 추가
        firestore.collection("userDataStock").document(itemUid).set(userData)
    }

    private fun deleteUserItemFromFirestore(title: String) {
        // Cloud Firestore에서 해당 UID를 가진 도큐먼트를 삭제
        firestore.collection("userDataStock").document(title).delete()//해당 uid에 해당하는 모든
        //제품 삭제
    }

}