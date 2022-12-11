package com.reindrairawan.organisasimahasiswa.data.searchview.remote.api

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.KegiatanResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface KegiatanApi {
    @GET("search/{nama}")
    suspend fun searchKegiatan(@Path("nama") nama:String) : Response<WrappedListResponse<KegiatanResponse>>

    @GET("kegiatan")
    suspend fun getKegiatan(): Response<WrappedListResponse<KegiatanResponse>>
}