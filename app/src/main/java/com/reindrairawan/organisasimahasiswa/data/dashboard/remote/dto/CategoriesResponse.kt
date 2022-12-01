package com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("id") var id: Int,
    @SerializedName("nama_jenis_kegiatan") var name: String,
    @SerializedName("gambar") var path: String
)
