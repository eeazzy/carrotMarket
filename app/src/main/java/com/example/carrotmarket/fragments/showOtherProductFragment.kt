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
import com.example.carrotmarket.databinding.FragmentEditproductBinding
import com.example.carrotmarket.databinding.FragmentShowotherproductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class showOtherProductFragment : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private var mBinding: FragmentShowotherproductBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        mBinding = FragmentShowotherproductBinding.inflate(inflater, container, false)

        var getProductUid:String=""
        var productTitle = arguments?.getString("otherTitle")
        var getName:String=""
        val docRef = firestore.collection("userDataStock").document(productTitle.toString())//데이터베이스에서 get
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    mBinding?.otherTitle?.setText(document.getString("title"))
                    mBinding?.otherPrice?.setText(document.getDouble("price").toString())
                    mBinding?.otherDetails?.setText(document.getString("detail"))
                    getProductUid=document.getString("uid").toString()
                    getName=document.getString("title").toString()
                }
            }
            .addOnFailureListener { exception ->
            }
        val otherBackHomeBtn=mBinding?.otherBackHomeBtn
        val moveToChatRoom=mBinding?.startChatBtn
        otherBackHomeBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_showOtherProductFragment_to_homeFragment2)//프래그먼트의 이동
            this.onDestroyView()
        }

        moveToChatRoom?.setOnClickListener {
           val action3= showOtherProductFragmentDirections.actionShowOtherProductFragmentToChattingFragment(getProductUid)
            findNavController().navigate(action3)
            this.onDestroyView()
        }
        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}