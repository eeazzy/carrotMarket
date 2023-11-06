package com.example.carrotmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.carrotmarket.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
//네비 담기 HOST
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment
//네비게이션 컨트롤
        val navController=navHostFragment.navController
        //바텀 네비게이션과 묶기
        NavigationUI.setupWithNavController(mBinding.bottomHamster, navController)
    }
}
