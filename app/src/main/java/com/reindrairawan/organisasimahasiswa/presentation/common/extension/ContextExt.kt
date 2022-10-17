package com.reindrairawan.organisasimahasiswa.presentation.common.extension

import android.content.Context
import android.widget.Toast
import com.reindrairawan.organisasimahasiswa.R

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showGenericAlertDialog(message: String) {
    androidx.appcompat.app.AlertDialog.Builder(this).apply {
        setMessage(message)

        setPositiveButton(getString(R.string.button_text_ok)) { dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}