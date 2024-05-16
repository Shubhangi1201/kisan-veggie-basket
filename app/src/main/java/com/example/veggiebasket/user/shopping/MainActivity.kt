package com.example.veggiebasket.user.shopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.authentication.presentation.AuthActivity
import com.example.veggiebasket.authentication.presentation.viewmodel.AuthViewModel
import com.example.veggiebasket.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var  navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.ShoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemReselectedListener {
            when(it.itemId){
                R.id.homeFragment ->{
                    navController.navigate(R.id.homeFragment)
                }

                R.id.cartFragment ->{
                    navController.navigate(R.id.cartFragment)
                }

                R.id.profileFragment ->{
                    navController.navigate(R.id.profileFragment)
                }

                R.id.myorderMain2fragment->{
                    navController.navigate(R.id.myorderMain2fragment)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutUser -> {
                Toast.makeText(this, "logout user", Toast.LENGTH_SHORT).show()
                authViewModel.logout()
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()

                return true
            }
            // Add cases for other menu items as needed
            else -> return super.onOptionsItemSelected(item)
        }
    }


}