package com.bucharest.qurio.presentation.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bucharest.qurio.presentation.constants.PresentationConstants

object ViewUtils {
    
    fun showToast(fragment: Fragment, message: String, duration: Int = PresentationConstants.TOAST_DURATION_SHORT) {
        Toast.makeText(fragment.requireContext(), message, duration).show()
    }
    
    fun showErrorToast(fragment: Fragment, message: String) {
        showToast(fragment, "Error: $message")
    }
    
}
