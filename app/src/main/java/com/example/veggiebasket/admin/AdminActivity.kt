package com.example.veggiebasket.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.authentication.presentation.AuthActivity
import com.example.veggiebasket.authentication.presentation.viewmodel.AuthViewModel
import com.example.veggiebasket.databinding.ActivityAdminBinding
import com.example.veggiebasket.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    val binding : ActivityAdminBinding by lazy {
        ActivityAdminBinding.inflate(layoutInflater)
    }
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbaradmin)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.ShoppingHostFragmentqwe)
        binding.bottomNavigationqwe.setupWithNavController(navController)




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
                startActivity(Intent(this@AdminActivity, AuthActivity::class.java))
                finish()

                return true
            }
            // Add cases for other menu items as needed
            else -> return super.onOptionsItemSelected(item)
        }
    }
}