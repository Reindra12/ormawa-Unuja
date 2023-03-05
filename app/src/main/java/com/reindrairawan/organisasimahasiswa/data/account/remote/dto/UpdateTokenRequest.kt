package com.reindrairawan.organisasimahasiswa.data.account.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateTokenRequest(
    @SerializedName("fcm_token") val fcmToken: String,

    )