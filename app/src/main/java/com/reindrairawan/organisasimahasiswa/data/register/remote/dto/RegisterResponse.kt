package com.reindrairawan.organisasimahasiswa.data.register.remote.dto

import com.google.gson.annotations.SerializedName

class RegisterResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("token") var token: String? = null

)
