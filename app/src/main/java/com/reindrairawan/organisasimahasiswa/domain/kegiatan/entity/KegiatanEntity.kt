package com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity

data class KegiatanEntity(
    var id: Int,
    var nama_kegiatan: String,
    var diskripsi_kegiatan: String,
    var gambar_kegiatan: String,
    val tgl_kegiatan: String,
    val jam_kegiatan: String,
    val tempat: String

    )