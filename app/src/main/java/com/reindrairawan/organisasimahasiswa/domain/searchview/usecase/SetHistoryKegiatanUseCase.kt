package com.reindrairawan.organisasimahasiswa.domain.searchview.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.searchview.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.HistoryKegiatanEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetHistoryKegiatanUseCase @Inject constructor(private val kegiatanRepository: KegiatanRepository) {
    suspend fun invoke(historyKegiatanRequest: HistoryKegiatanRequest): Flow<BaseResult<HistoryKegiatanEntity, WrappedResponse<HistoryPencarianResponse>>> {
        return kegiatanRepository.setHistoryKegiatan(historyKegiatanRequest)
    }
}