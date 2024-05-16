package com.example.veggiebasket.admin.fragments.admin_myaccount_screen

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentAdminnPersonalDetailsBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.PersonalDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AdminnPersonalDetailsFragment : Fragment() {
    private lateinit var binding: FragmentAdminnPersonalDetailsBinding
    private val viewmodel: PersonalDetailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAdminnPersonalDetailsBinding.inflate(inflater)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTextDate11.setOnClickListener{
                addDatePicker()
            }
            saveBtn11.setOnClickListener {
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
                binding.editTextDate11.text = selectedDate
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun ValidateDetails(){
        binding.apply {
            if(PersonalName11.text.toString().isNullOrEmpty()){
                PersonalName11.setError("please fill this input field")
            }else if(PersonalEmail11.text.toString().isNullOrEmpty()){
                PersonalEmail11.setError("please fill this input field")
            }else if(PersonalPhone11.text.toString().isNullOrEmpty()){
                PersonalPhone11.setError("Please fill this input field")
            }else if(editTextDate11.text.toString().isNullOrEmpty()){
                editTextDate11.setError("Please select date of birth")
            }else{
                uplaodToDatabase()
            }
        }
    }

    private fun uplaodToDatabase(){
        val details : PersonalData
        binding.apply {
            details = PersonalData(PersonalName11.text.toString(),PersonalEmail11.text.toString(), PersonalPhone11.text.toString(), editTextDate11.text.toString())
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
                    findNavController().navigate(R.id.action_adminnPersonalDetailsFragment_to_adminProfileFragment2)
                }
            }
        }
    }
}