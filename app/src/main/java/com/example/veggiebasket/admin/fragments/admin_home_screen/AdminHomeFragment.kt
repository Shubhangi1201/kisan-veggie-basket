package com.example.veggiebasket.admin.fragments.admin_home_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.veggiebasket.R
import com.example.veggiebasket.admin.AdminActivity
import com.example.veggiebasket.authentication.presentation.AuthActivity
import com.example.veggiebasket.authentication.presentation.viewmodel.AuthViewModel
import com.example.veggiebasket.databinding.FragmentAdminHomeBinding
import com.example.veggiebasket.user.shopping.data.CategoryEnum
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminHomeFragment : Fragment() {
    private lateinit var binding: FragmentAdminHomeBinding
    private lateinit var imageUriList: ArrayList<String>
    private lateinit var dbref: DatabaseReference
    private var userkey: String? = null
    private var category: String? = null

    private val selectedImages = mutableListOf<Uri>()

    private val authViewModel by viewModels<AuthViewModel>()

    companion object {
        const val PICK_IMAGES_REQUEST = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Kisan Veggie Basket"

        imageUriList = arrayListOf()

        dbref = FirebaseDatabase.getInstance().getReference("Admin")
        val categoryItem = arrayOf(
            CategoryEnum.Special.displayName,
            CategoryEnum.Vegetables.displayName,
            CategoryEnum.Fruits.displayName,
            CategoryEnum.DryFruite.displayName,
            CategoryEnum.FreshBakery.displayName,
            CategoryEnum.Exotic.displayName,
            CategoryEnum.Vstaples.displayName,
            CategoryEnum.FreshAtta.displayName,
            CategoryEnum.MomKitchen.displayName,
            CategoryEnum.ColdOil.displayName,
            CategoryEnum.MasalaBlend.displayName
        )
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryItem)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOptions.adapter = adapter

        binding.spinnerOptions.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = categoryItem[position]
                    category = selectedItem
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing here
                }
            }

        binding.apply {
            floatingActionButtonAdmin.setOnClickListener {
                clearForm()
            }

            uploadImagesBtn.setOnClickListener {
//                addProductProgessbar.visibility = View.VISIBLE
//                uploadImagesBtn.visibility = View.GONE
                uploadImagesBtn.isEnabled = false
                fetchImages()
            }

            addProductBtn.setOnClickListener {
                validateDetails()
            }
        }
    }

    private fun validateDetails() {
        binding.apply {
            if (vegeName.text.toString().isNullOrEmpty()) {
                vegeName.setError("Invalid Input Field")
            } else if (vegePrice.text.toString().isNullOrEmpty()) {
                vegePrice.setError("Invalid input field")
            } else if (vegeWeight.text.toString().isNullOrEmpty()) {
                vegeWeight.setError("Invalid input field")
            } else if (vegeDescription.text.toString().isNullOrEmpty()) {
                vegeDescription.setError("Invalid input filed")
            } else if (vegeOfferpercentage.text.toString().isNullOrEmpty()) {
                vegeOfferpercentage.setError("Invalid input filed")
            } else if (imageUriList.size == 0) {
                Toast.makeText(requireContext(), "upload minimum one image", Toast.LENGTH_SHORT)
                    .show()
            } else if (category == null) {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT)
                    .show()
            } else {
//                progressBar2.visibility = View.VISIBLE
//                addProductBtn.visibility = View.GONE
                  addProductBtn.isEnabled = false
                saveAllDataToRealtimeDatabase(
                    vegeName.text.toString(),
                    vegePrice.text.toString(),
                    vegeWeight.text.toString(),
                    vegeDescription.text.toString(),
                    vegeOfferpercentage.text.toString()
                )
            }
        }
    }

    private fun fetchImages() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMAGES_REQUEST)
    }

    private fun clearForm() {
        binding.apply {
            // Clear EditText fields
            vegeName.text = null
            vegePrice.text = null
            vegeWeight.text = null
            vegeDescription.text = null
            vegeOfferpercentage.text = null

            // Clear spinner selection
            spinnerOptions.setSelection(0)

            // Clear imageUriList
            imageUriList.clear()
        }
    }

    private fun saveAllDataToRealtimeDatabase(
        name: String,
        price: String,
        weight: String,
        description: String,
        offer: String
    ) {
        val productKey = dbref.child(userkey!!).child("MyProducts").push().key.toString()

        userkey = productKey
        val sellerName = authViewModel.currentUser?.displayName.toString()

        val imageMap = generateRandomKeyMap(imageUriList)

        val product = FetchProduct(
            userkey!!,
            name,
            description,
            category!!,
            price,
            offer,
            imageMap,
            sellerName,
            weight
        )

        dbref.child(userkey!!).child("MyProducts").child(productKey).setValue(product)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.uploadProgressBar.visibility = View.GONE
                    binding.addProductBtn.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "product details uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    clearForm()
                } else {
                    binding.addProductProgessbar.visibility = View.GONE
                    binding.addProductBtn.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "failed to upload", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun emailKey(emailAddress: String): String {
        val parts = emailAddress.split("@")
        return parts[0]
    }

    private fun generateRandomKeyMap(arrayList: ArrayList<String>): Map<String, String> {
        userkey = emailKey(authViewModel.currentUser?.email!!)

        val map = mutableMapOf<String, String>()
        arrayList.forEachIndexed { index, item ->
            val key = generateRandomKey(userkey.toString())
            map[key] = item
        }

        return map
    }

    private fun generateRandomKey(userkey: String, length: Int = 10): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val randomString = (1..length).map { allowedChars.random() }.joinToString("")
        return userkey + randomString
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.uploadImagesBtn.isEnabled = true
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val imageUrls = mutableListOf<String>()
            val clipData = data?.clipData

            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    val imageName = "image_$i.jpg"
                    uploadImage(imageUri, imageName)
                }
            } else {
                val imageUri = data?.data
                val imageName = "image.jpg"
                uploadImage(imageUri, imageName)
            }
        }
    }

    private fun uploadImage(imageUri: Uri?, imageName: String) {
        binding.uploadProgressBar.visibility = View.VISIBLE
        binding.uploadImagesBtn.visibility = View.GONE
        val timestamp = System.currentTimeMillis().toString()
        val storageRef = FirebaseStorage.getInstance().reference.child("images/$timestamp")
        val uploadTask = storageRef.putFile(imageUri!!)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                // Get the download URL and save it to the Realtime Database
                imageUriList.add(uri.toString())
                binding.apply {
                    uploadProgressBar.visibility = View.GONE
                    uploadImagesBtn.visibility = View.VISIBLE
                }
            }
        }.addOnFailureListener { exception ->
            // Handle failed upload
            Toast.makeText(requireContext(), "Failed to upload: ${exception.message}", Toast.LENGTH_SHORT).show()
            binding.apply {
                uploadProgressBar.visibility = View.GONE
                uploadImagesBtn.visibility = View.VISIBLE
            }
        }
    }

}
