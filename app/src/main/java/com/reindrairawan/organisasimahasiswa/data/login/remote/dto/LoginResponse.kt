package com.reindrairawan.organisasimahasiswa.data.login.remote.dto

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("id_mahasiswa") var id: Int? = null,
    @SerializedName("nama") var nama: String? = null,
    @SerializedName("nim") var nim: String? = null,
    @SerializedName("user") var user: String? = null,
    @SerializedName("token") var token: String? = null

)