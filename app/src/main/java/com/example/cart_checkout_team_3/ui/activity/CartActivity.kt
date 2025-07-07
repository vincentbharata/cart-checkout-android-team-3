package com.example.cart_checkout_team_3.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.ActivityCartBinding
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar first
        setSupportActionBar(binding.toolbar)

        // Enable menu on toolbar
        binding.toolbar.inflateMenu(R.menu.menu_main)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        // Wait for the fragment to be properly attached before setting up navigation
        supportFragmentManager.executePendingTransactions()

        try {
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.fragmentCartList, R.id.fragmentProductPicker, R.id.fragmentCartHistory)
            )

            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.bottomNavigation.setupWithNavController(navController)

            setupClickListeners()
            observeUser()

            // Don't automatically load products - only load when user is logged in
        } catch (e: Exception) {
            // If navigation setup fails, set up basic functionality without navigation
            setupClickListeners()
            observeUser()
        }
    }

    private fun setupClickListeners() {
        binding.fabAddToCart.setOnClickListener {
            try {
                findNavController(R.id.nav_host_fragment).navigate(R.id.fragmentProductPicker)
            } catch (e: Exception) {
                // Fallback jika navigation gagal
            }
        }

        // Setup bottom navigation click manually jika auto setup gagal
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            try {
                val navController = findNavController(R.id.nav_host_fragment)
                when (item.itemId) {
                    R.id.fragmentCartList -> {
                        navController.navigate(R.id.fragmentCartList)
                        true
                    }
                    R.id.fragmentProductPicker -> {
                        navController.navigate(R.id.fragmentProductPicker)
                        true
                    }
                    R.id.fragmentCartHistory -> {
                        navController.navigate(R.id.fragmentCartHistory)
                        true
                    }
                    else -> false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun observeUser() {
        viewModel.user.observe(this) { user ->
            if (user == null) {
                // User logged out, navigate back to login
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // User is logged in, load their cart and products
                viewModel.loadCartForUser(user.id)
                viewModel.loadProducts()
            }
        }

        // Observe cart changes to update UI
        viewModel.cartItems.observe(this) { cartItems ->
            // Update bottom navigation badge if needed
            val cartCount = cartItems.sumOf { it.quantity }
            // You can add badge logic here if needed
        }

        // Observe products to ensure they're loaded
        viewModel.products.observe(this) { products ->
            if (products.isEmpty()) {
                // Retry loading products if empty
                viewModel.loadProducts()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Show confirmation dialog before logout
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.logout()
                        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.action_checkout -> {
                try {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.fragmentCartSummary)
                } catch (e: Exception) {
                    Toast.makeText(this, "Please go to cart first", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
