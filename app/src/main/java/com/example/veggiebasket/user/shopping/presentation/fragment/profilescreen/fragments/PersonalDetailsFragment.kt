package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentPersonalDetailsBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.PersonalDetailViewModel
import java.util.Calendar


class PersonalDetailsFragment : Fragment() {
    private lateinit var binding: FragmentPersonalDetailsBinding
    private val viewmodel: PersonalDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalDetailsBinding.inflate(inflater)



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTextDate.setOnClickListener{
                addDatePicker()
            }

            saveBtn.setOnClickListener {
                ValidateDetails()
            }
        }
    }

    fun addDatePicker(){
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog and show it
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Handle the selected date
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                // You can use the selected date here as needed
                // For example, set it to an EditText field
                binding.editTextDate.text = selectedDate
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun ValidateDetails(){
        binding.apply {
            if(PersonalName.text.toString().isNullOrEmpty()){
                PersonalName.setError("please fill this input field")
            }else if(PersonalEmail.text.toString().isNullOrEmpty()){
                PersonalEmail.setError("please fill this input field")
            }else if(PersonalPhone.text.toString().isNullOrEmpty()){
                PersonalPhone.setError("Please fill this input field")
            }else if(editTextDate.text.toString().isNullOrEmpty()){
                editTextDate.setError("Please select date of birth")
            }else{
                uplaodToDatabase()
            }
        }
    }

    private fun uplaodToDatabase(){
        val details : PersonalData
        binding.apply {
            details = PersonalData(PersonalName.text.toString(),PersonalEmail.text.toString(), PersonalPhone.text.toString(), editTextDate.text.toString())
        }
        viewmodel.uploadPersonalData(details)
        viewmodel.uploadData.observe(viewLifecycleOwner){resource->
            when(resource){
                is CategoryResource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()

                }
                is CategoryResource.Loading ->{

                }
                is CategoryResource.Success ->{
                    findNavController().navigate(R.id.action_personalDetailsFragment_to_profileFragment)
                }
            }
        }
    }

}