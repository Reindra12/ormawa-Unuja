package com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto

import com.google.gson.annotations.SerializedName

data class HistoryKegiatanRequest(
    @SerializedName("judul") val judul : String,
    @SerializedName("id_mahasiswa") val id_mahasiswa : Int,

)
