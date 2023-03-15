package com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.api

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.KegiatanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface KegiatanApi {

    @GET("kegiatanByIdJenis/{id}")
    suspend fun kegiatanByIdJenis(@Path("id") id: Int): Response<WrappedListResponse<KegiatanResponse>>

    @GET("historypencarian/{id_mahasiswa}")
    suspend fun searchKegiatan(@Path("id_mahasiswa") id: Int): Response<WrappedListResponse<HistoryPencarianResponse>>

    @POST("historypencarian")
    suspend fun setHistoryKegiatan(@Body historyKegiatanRequest: HistoryKegiatanRequest): Response<WrappedResponse<HistoryPencarianResponse>>

    @GET("kegiatan")
    suspend fun getKegiatan(): Response<WrappedListResponse<KegiatanResponse>>
}