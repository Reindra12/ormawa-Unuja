package com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("path") var path: String
)
