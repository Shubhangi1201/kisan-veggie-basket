package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.product_details

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentSingleProductInfoBinding

import com.example.veggiebasket.user.shopping.data.PaymentEnum
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEvent
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEventProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.HomeFragmentDirections
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter.ImageSliderAdapter
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.HomeViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@AndroidEntryPoint
class SingleProductInfoFragment : Fragment(), PaymentResultListener {
    private lateinit var binding: FragmentSingleProductInfoBinding
    private lateinit var currentProduct: FetchProduct

    private val viewmodel: ProductDetailViewModel by activityViewModels()
    private var isvisible : Boolean = false
    private var quantity: Int = 0
    private var paymentMode: String ?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingleProductInfoBinding.inflate(inflater)


        viewmodel.prductData.observe(viewLifecycleOwner){product->
            binding.productPrice.text = product.toString()
            currentProduct = product
            currentProduct.let {
                updateUiWithProduct(currentProduct!!)
            }
            Log.d("asdb", "i am setting thisinside viewmodel ahahahahha")
        }


        return binding.root
    }

    private fun updateUiWithProduct(currentProduct: FetchProduct){
        binding.productPrice.text = "₹${currentProduct.price}"
        val list = currentProduct.imageUrl.values.toList()
        Log.d("ddd", "this is update ui list${list.toString()} + this is imageurl ${currentProduct.imageUrl}")
        val adapter = ImageSliderAdapter(requireContext(), list)
        binding.productImageViewPager.adapter = adapter

        val dotsIndicator = binding.wormDotsIndicator
        dotsIndicator.attachTo(binding.productImageViewPager)

        binding.OfferPercentageApplied.text = "After Offer Applied ${currentProduct.offerPercentage}%"
        binding.productDesc.text = currentProduct.description
        binding.sellerNameInfo.text = "Sold by : ${currentProduct.sellerName}"

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SPaddtocart.setOnClickListener{
           viewmodel.prductData.observe(viewLifecycleOwner){
               viewmodel.addToCart(it)
           }
        }

        binding.quantityTextView.text = quantity.toString()


        binding.apply {
            ll2.setOnClickListener{
                if(isvisible){
                    productDesc.visibility = View.GONE
                    expandBtn.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                    isvisible = false


                }else{
                    productDesc.visibility = View.VISIBLE
                    expandBtn.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
                    isvisible = true
                }
            }

            plusButton.setOnClickListener{
                increaseQuantity()
            }

            minusButton.setOnClickListener{
                decreaseQuantity()
            }


        }

        binding.paymentMethodRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Find which radio button is selected by id
            // Now you can handle the result based on which radio button is selected
            when (checkedId) {
                R.id.cashOnDeliveryRadioButton -> {
                    // Handle cash on delivery selection
                    paymentMode = PaymentEnum.CASH.toString()
                }
                R.id.onlinePaymentRadioButton -> {
                    // Handle online payment selection
                    paymentMode = PaymentEnum.ONLINE.toString()
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.orderResult.observe(viewLifecycleOwner){resource->
                when(resource){
                    is CategoryResource.Error -> {
                        Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Loading -> {
                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()

                    }
                    is CategoryResource.Success -> {
                        Toast.makeText(requireContext(), "details uploaded successfully", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.AdcResult.observe(viewLifecycleOwner){resource->
                when(resource){
                    is CategoryResource.Error ->{
                        Toast.makeText(requireContext(), "Error : ${resource.message}", Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Loading -> {
                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()

                    }
                    is CategoryResource.Success ->{
                        Toast.makeText(requireContext(), "${resource.data}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//        binding.apply {
//            llprice.text = "₹${currentProduct!!.price}"
//        }


        binding.SPbuynow.setOnClickListener{
            currentProduct.let {
                checkDetailsValidation()

            }

        }
    }


    private fun makePayment() {

        val activity = requireActivity()

        // Ensure that the checkout instance is properly initialized
        Checkout.preload(activity.applicationContext)

        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","SSJ")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");

            options.put("amount","50000")//pass amount in currency subunits


            val prefill = JSONObject()
            prefill.put("email","")
            prefill.put("contact","")

            options.put("prefill",prefill)
            co.open(activity,options)
            findNavController().navigate(R.id.action_singleProductInfoFragment_to_homeFragment)

        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
            findNavController().navigate(R.id.action_singleProductInfoFragment_to_homeFragment)

        }

    }

    override fun onPaymentSuccess(p0: String?) {
    }

    override fun onPaymentError(p0: Int, p1: String?) {

    }

    private fun checkDetailsValidation(){
        val date =getCurrentDateWithCalendar()
        if (quantity != 0){
                if(currentProduct!=null){
                    val orderhash = MyOrderData(
                        currentProduct.id,
                        currentProduct.name,
                        currentProduct.description,
                        currentProduct.category,
                        currentProduct.price,
                        currentProduct.offerPercentage,
                        currentProduct.imageUrl,
                        currentProduct.sellerName,
                        date,
                        quantity.toString(),
                        currentProduct.weight

                        )
                if(paymentMode == PaymentEnum.CASH.toString()){
                    Toast.makeText(requireContext(), "Your order has been placed successfully", Toast.LENGTH_SHORT).show()


                    viewmodel.myOrder(orderhash)
                }else if (paymentMode == PaymentEnum.ONLINE.toString()){
                    makePayment()
                    viewmodel.myOrder(orderhash)
                }else if(paymentMode == null){
                    Toast.makeText(requireContext(), "Please select mode of payment", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "please select quantity", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(), "product details are null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseQuantity() {
        quantity++
        binding.quantityTextView.text = quantity.toString()
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        currentProduct.let {
            val totalPrice = quantity * currentProduct.price.toInt()
            val df = DecimalFormat("#.##")

            binding.apply {
                lltotalamount.text = "₹${df.format(totalPrice)}"
                llprice.text = "₹${currentProduct.price}"
                llquantity.text = "${quantity}"
                SPbuynow.text = "Pay ₹${df.format(totalPrice)}"
            }
        }
    }

    private fun decreaseQuantity() {
        if (quantity > 0) {
            quantity--
            binding.quantityTextView.text = quantity.toString()
            updateTotalPrice()
        }
    }



    private fun getCurrentDateWithCalendar(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day-$month-$year"
    }

}