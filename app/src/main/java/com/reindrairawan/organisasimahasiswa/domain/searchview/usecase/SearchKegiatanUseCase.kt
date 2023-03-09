package com.reindrairawan.organisasimahasiswa.domain.searchview.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.searchview.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.HistoryKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchKegiatanUseCase @Inject constructor(private val kegiatanRepository: KegiatanRepository) {
    suspend fun invoke(id:Int):Flow<BaseResult<List<HistoryKegiatanEntity>, WrappedListResponse<HistoryPencarianResponse>>> {
        return kegiatanRepository.searchKegiatan(id)
    }
}
