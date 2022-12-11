package com.reindrairawan.organisasimahasiswa.data.searchview.remote.api

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.KegiatanResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface KegiatanApi {
    @GET("historypencarian/{id_mahasiswa}")
    suspend fun searchKegiatan(@Path("id_mahasiswa") id: Int): Response<WrappedListResponse<HistoryPencarianResponse>>

    @GET("kegiatan")
    suspend fun getKegiatan(): Response<WrappedListResponse<KegiatanResponse>>
}