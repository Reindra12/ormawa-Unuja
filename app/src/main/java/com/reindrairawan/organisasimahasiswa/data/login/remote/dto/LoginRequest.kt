package com.reindrairawan.organisasimahasiswa.data.login.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("user") val user: String,
    @SerializedName("password") val password: String,
    )