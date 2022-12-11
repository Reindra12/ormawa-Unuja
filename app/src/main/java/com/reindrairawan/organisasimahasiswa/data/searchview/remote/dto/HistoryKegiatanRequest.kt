package com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto

import com.google.gson.annotations.SerializedName

data class HistoryKegiatanRequest(
    @SerializedName("judul") val judul : String,
    @SerializedName("waktu") val waktu : String,
    @SerializedName("id_mahasiswa") val id_mahasiswa : Int,

)
