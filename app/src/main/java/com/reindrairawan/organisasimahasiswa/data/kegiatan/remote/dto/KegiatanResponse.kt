package com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto

import com.google.gson.annotations.SerializedName

data class KegiatanResponse(

    @SerializedName("id_kegiatan") var id_kegiatan : Int,
    @SerializedName("nama_kegiatan") var nama_kegiatan: String,
    @SerializedName("diskripsi_kegiatan") var diskripsi_kegiatan: String,
    @SerializedName("gambar_kegiatan") var gambar_kegiatan: String,
    @SerializedName("tgl_kegiatan") var tgl_kegiatan: String,
    @SerializedName("jam_kegiatan") var jam_kegiatan: String,
    @SerializedName("id_ormawa") var id_ormawa: Int,
    @SerializedName("id_jenis_kegiatan") var id_jenis_kegiatan: Int,
    @SerializedName("tempat") var tempat:String,
    @SerializedName("hari") var hari:String


)