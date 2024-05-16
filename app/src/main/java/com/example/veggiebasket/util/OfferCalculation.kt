package com.example.veggiebasket.util

object OfferCalculation {
    fun calculateDiscountedAmount(originalAmount: Double, discountPercentage: Double): Double {
        val discount = originalAmount * (discountPercentage / 100)
        return originalAmount - discount
    }
}