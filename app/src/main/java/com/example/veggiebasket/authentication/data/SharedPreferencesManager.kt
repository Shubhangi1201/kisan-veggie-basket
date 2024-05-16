package com.example.veggiebasket.authentication.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.veggiebasket.authentication.domain.model.UserType

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "UserPrefs",
        Context.MODE_PRIVATE
    )

    fun saveUserType(userType: UserType) {
        sharedPreferences.edit().putString("userType", userType.name).apply()
        Log.d("abcd", "${userType} saved")
    }

    fun getUserType(): UserType {
        val userTypeString = sharedPreferences.getString("userType", UserType.USER.name)
        Log.d("abcd", "${userTypeString} getusertype")

        return UserType.valueOf(userTypeString ?: UserType.USER.name)
    }
}
