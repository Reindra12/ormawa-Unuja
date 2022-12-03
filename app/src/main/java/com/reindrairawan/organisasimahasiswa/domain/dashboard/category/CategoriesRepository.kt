package com.reindrairawan.organisasimahasiswa.domain.dashboard.category

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.CategoriesResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanResponse
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginRequest
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.entity.JenisKegiatanEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CategoriesRepository {
    suspend fun getCategories(): Flow<BaseResult<List<CategoriesEntity>, WrappedListResponse<CategoriesResponse>>>

    suspend fun postJenisKegiatan(file:MultipartBody.Part, name:RequestBody): Flow<BaseResult<JenisKegiatanEntity, WrappedResponse<JenisKegiatanResponse>>>

}