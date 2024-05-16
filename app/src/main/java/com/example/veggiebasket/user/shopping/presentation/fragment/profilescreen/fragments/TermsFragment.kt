package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.veggiebasket.R


class TermsFragment : Fragment() {


    val webUrl = "https://sites.google.com/view/veggiebasketterms/home"

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_terms, container, false)

        webView = view.findViewById(R.id.webView1)
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.loadUrl(webUrl) // Replace with your privacy policy URL

        return view
    }

}