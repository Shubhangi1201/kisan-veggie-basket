package com.example.veggiebasket.authentication.presentation.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.veggiebasket.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
){
    val dialog = BottomSheetDialog(requireContext(), R.style.dialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.setContentView(view)
    dialog.show()

    val edEmail = view.findViewById<EditText>(R.id.etResetPassword)
    val cancelBtn = view.findViewById<Button>(R.id.cancelResetBtn)
    val sendBtn = view.findViewById<Button>(R.id.sendResentBtn)

    sendBtn.setOnClickListener{
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    cancelBtn.setOnClickListener{
        dialog.dismiss()
    }

}