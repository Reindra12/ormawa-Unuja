package com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto

import com.google.gson.annotations.SerializedName

data class DaftarKegiatanRequest(
    @SerializedName("id_mahasiswa") val id_mahasiswa: Int,
    @SerializedName("id_kegiatan") val id_kegiatan : Int,
    @SerializedName("status") val status : String
)
