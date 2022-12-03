package com.reindrairawan.organisasimahasiswa.data.dashboard.remote.api


import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.CategoriesResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CategoriesApi {
    @GET("jenis_kegiatan")
    suspend fun getCategories(): Response<WrappedListResponse<CategoriesResponse>>


    @Multipart
    @POST("jenis_kegiatan")
    suspend fun tambahJenisKegiatan(@Part file: MultipartBody.Part, @Part("nama_jenis_kegiatan") namaJenisKegiatan:RequestBody): Response<WrappedResponse<JenisKegiatanResponse>>
}