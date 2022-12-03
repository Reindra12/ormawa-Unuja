package com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto

import com.google.gson.annotations.SerializedName

data class JenisKegiatanRequest(
    @SerializedName("nama_jenis_kegiatan") val nama: String,
    @SerializedName("gambar_jenis_kegiatan") val gambar: String,
//    @SerializedName("status") val status: String,
)
