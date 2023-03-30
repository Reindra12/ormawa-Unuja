package com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.DaftarKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.DaftarKegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.DaftarKegiatanEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DaftarKegiatanUseCase @Inject constructor(private val kegiatanRepository: KegiatanRepository){

    suspend fun invoke(daftarKegiatanRequest: DaftarKegiatanRequest): Flow<BaseResult<DaftarKegiatanEntity, WrappedResponse<DaftarKegiatanResponse>>>{
        return kegiatanRepository.daftarKegiatan(daftarKegiatanRequest)
    }
 }
