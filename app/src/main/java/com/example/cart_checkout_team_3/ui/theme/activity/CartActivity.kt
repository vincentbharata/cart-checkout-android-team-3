package com.example.cart_checkout_team_3.ui.theme.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.cart_checkout_team_3.R

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val userId = intent.getIntExtra("USER_ID", -1)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (savedInstanceState == null) {
            val bundle = Bundle().apply {
                putInt("USER_ID", userId)
            }
            navController.setGraph(R.navigation.nav_graph, bundle)
        }
    }
}
