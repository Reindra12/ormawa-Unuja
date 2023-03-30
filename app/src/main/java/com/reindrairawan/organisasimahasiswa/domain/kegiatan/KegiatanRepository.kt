package com.reindrairawan.organisasimahasiswa.domain.kegiatan

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.*
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.DaftarKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.HistoryKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import kotlinx.coroutines.flow.Flow

interface KegiatanRepository {

    suspend fun kegiatanByIdJenis(id:Int) :Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>>
    suspend fun searchKegiatan(id: Int): Flow<BaseResult<List<HistoryKegiatanEntity>, WrappedListResponse<HistoryPencarianResponse>>>
    suspend fun getAllKegiatan(): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>>
    suspend fun setHistoryKegiatan(historyKegiatanRequest: HistoryKegiatanRequest): Flow<BaseResult<HistoryKegiatanEntity, WrappedResponse<HistoryPencarianResponse>>>
    suspend fun detailKegiatan(id: Int): Flow<BaseResult<KegiatanEntity, WrappedResponse<KegiatanResponse>>>
    suspend fun daftarKegiatan(daftarKegiatanRequest: DaftarKegiatanRequest):Flow<BaseResult<DaftarKegiatanEntity,WrappedResponse<DaftarKegiatanResponse>>>
}