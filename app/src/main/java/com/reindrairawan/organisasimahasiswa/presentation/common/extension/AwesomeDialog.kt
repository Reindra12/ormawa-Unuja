package com.reindrairawan.organisasimahasiswa.presentation.common.extension

import android.app.Activity
import android.content.Context
import com.example.awesomedialog.*
import com.reindrairawan.organisasimahasiswa.R


fun Context.AwesomeDialogMessage(
    context: Context,
    positveMessage: String,
    negativeMessage: String,
    callback: ((String) -> Unit)? = null,
//    negativeButtonFunction:(()-> Unit)? = null

) {
    AwesomeDialog.build(context as Activity).title(getString(R.string.dialog_camera))
        .body(getString(R.string.dialog_body_camera)).icon(R.drawable.camera_dialog)
        .onPositive(positveMessage) {
            callback?.invoke(positveMessage)
        }.onNegative(negativeMessage) {
            callback?.invoke(negativeMessage)
        }
}

fun Context.AwesomeDialogSuccess(context: Context, title: String, body: String) {
    AwesomeDialog.build(context as Activity).title(title).body(body).icon(R.drawable.ic_tick)
}




