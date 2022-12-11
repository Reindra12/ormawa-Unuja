package com.reindrairawan.organisasimahasiswa.domain.searchview

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import kotlinx.coroutines.flow.Flow

interface KegiatanRepository {
    suspend fun searchKegiatan(nama: String): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>>
    suspend fun getAllKegiatan(): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>>
}