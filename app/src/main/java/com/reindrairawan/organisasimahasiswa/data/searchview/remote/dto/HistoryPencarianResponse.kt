package com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto

import com.google.gson.annotations.SerializedName

data class HistoryPencarianResponse(
    @SerializedName("id") var id:Int,
    @SerializedName("judul") var judul:String,
    @SerializedName("waktu") var waktu:String,
//    @SerializedName("gambar_kegiatan") var gambar_kegiatan:String,
//    @SerializedName("tgl_kegiatan") var tgl_kegiatan:String,
//    @SerializedName("jam_kegiatan") var jam_kegiatan:String,
//    @SerializedName("id_ormawa") var id_ormawa:Int,
)
