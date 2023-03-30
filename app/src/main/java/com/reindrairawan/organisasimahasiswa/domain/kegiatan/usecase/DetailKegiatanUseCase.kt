package com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailKegiatanUseCase @Inject constructor(private val kegiatanRepository: KegiatanRepository) {
    suspend fun invoke(id : Int) : Flow<BaseResult<KegiatanEntity, WrappedResponse<KegiatanResponse>>>{
        return kegiatanRepository.detailKegiatan(id)
    }
}