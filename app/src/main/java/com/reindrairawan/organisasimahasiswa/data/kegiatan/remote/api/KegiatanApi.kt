package com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.api

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.*
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

    @GET("kegiatan/{id}")
    suspend fun detailKegiatan (@Path("id") id : Int): Response<WrappedResponse<KegiatanResponse>>

    @POST("detail_kegiatan")
    suspend fun daftarkegiatan(@Body daftarKegiatanRequest:DaftarKegiatanRequest) : Response<WrappedResponse<DaftarKegiatanResponse>>
}