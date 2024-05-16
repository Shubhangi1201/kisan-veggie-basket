package com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain.edit_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.veggiebasket.databinding.FragmentAdminEditProductBinding


class AdminEditProductFragment : Fragment() {
    private lateinit var binding: FragmentAdminEditProductBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminEditProductBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}