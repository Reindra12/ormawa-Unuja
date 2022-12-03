package com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto

import com.google.gson.annotations.SerializedName

data class JenisKegiatanResponse(
    @SerializedName("nama_jenis_kegiatan") var nama: String? = null,
    @SerializedName("gambar_jenis_kegiatan") var gambar: String? = null,
//    @SerializedName("status") var status: String? = null,
    @SerializedName("id") var id: Int? = null
)
