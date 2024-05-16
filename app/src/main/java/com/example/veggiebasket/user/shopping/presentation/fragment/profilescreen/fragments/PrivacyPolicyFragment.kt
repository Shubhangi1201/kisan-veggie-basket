package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.veggiebasket.R

class PrivacyPolicyFragment : Fragment() {
    val webUrl = "https://sites.google.com/view/veggiebasket/home"

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_privacy_policy, container, false)

        webView = view.findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.loadUrl(webUrl) // Replace with your privacy policy URL

        return view
    }
}
