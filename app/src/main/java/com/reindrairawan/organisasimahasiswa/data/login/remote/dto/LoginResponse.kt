package com.reindrairawan.organisasimahasiswa.data.login.remote.dto

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("token") var token: String? = null
)