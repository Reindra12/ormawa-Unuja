package com.reindrairawan.organisasimahasiswa.data.account.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateTokenResponse(
    @SerializedName("id_mahasiswa") var id_mahasiswa: Int,
    @SerializedName("fcm_token") var fcmToken : String,
    @SerializedName("nama") var nama : String

)