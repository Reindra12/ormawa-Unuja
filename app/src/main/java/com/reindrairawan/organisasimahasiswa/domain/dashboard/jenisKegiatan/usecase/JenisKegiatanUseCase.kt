package com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.CategoriesRepository
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.entity.JenisKegiatanEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class JenisKegiatanUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {
    suspend fun invoke(file: MultipartBody.Part, name:RequestBody): Flow<BaseResult<JenisKegiatanEntity, WrappedResponse<JenisKegiatanResponse>>> {
        return categoriesRepository.postJenisKegiatan(file, name)
    }
}