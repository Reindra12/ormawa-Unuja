package com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.HistoryKegiatanEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetHistoryKegiatanUseCase @Inject constructor(private val kegiatanRepository: KegiatanRepository) {
    suspend fun invoke(historyKegiatanRequest: HistoryKegiatanRequest): Flow<BaseResult<HistoryKegiatanEntity, WrappedResponse<HistoryPencarianResponse>>> {
        return kegiatanRepository.setHistoryKegiatan(historyKegiatanRequest)
    }
}