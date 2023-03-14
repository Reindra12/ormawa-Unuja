package com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto

import com.google.gson.annotations.SerializedName

data class HistoryPencarianResponse(
    @SerializedName("id") var id:Int,
    @SerializedName("judul") var judul:String,
    @SerializedName("waktu") var waktu:String,
)
