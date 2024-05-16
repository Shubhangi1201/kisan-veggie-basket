package com.example.veggiebasket.authentication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.veggiebasket.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint
;

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    val binding : ActivityAuthBinding by lazy {
        ActivityAuthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}