package com.reindrairawan.organisasimahasiswa.data.searchview.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.api.KegiatanApi
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.HistoryKegiatanEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KegiatanRepositoryImpl @Inject constructor(private val kegiatanApi: KegiatanApi) :
    KegiatanRepository {
    override suspend fun searchKegiatan(id: Int): Flow<BaseResult<List<HistoryKegiatanEntity>, WrappedListResponse<HistoryPencarianResponse>>> {
        return flow {
            val response = kegiatanApi.searchKegiatan(id)
            if (response.isSuccessful) {
                val body = response.body()!!
                val history = mutableListOf<HistoryKegiatanEntity>()
                body.data?.forEach { historyPencarian ->
                    history.add(
                        HistoryKegiatanEntity(
                            historyPencarian.id,
                            historyPencarian.judul,
                            historyPencarian.waktu,

                            )
                    )
                }
                emit(BaseResult.Success(history))
            } else {
                val type =
                    object : TypeToken<WrappedListResponse<HistoryPencarianResponse>>() {}.type
                val err = Gson().fromJson<WrappedListResponse<HistoryPencarianResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                err.code = response.code()
                emit(BaseResult.Error(err))
                Log.d("searchKegiatan", err.toString())
            }
        }
    }

    override suspend fun getAllKegiatan(): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>> {
        return flow {
            val response = kegiatanApi.getKegiatan()
            if (response.isSuccessful) {
                val body = response.body()!!
                val kegiatan = mutableListOf<KegiatanEntity>()
                body.data?.forEach { kegiatanResponse ->
                    kegiatan.add(
                        KegiatanEntity(
                            kegiatanResponse.id_kegiatan,
                            kegiatanResponse.nama_kegiatan,
                            kegiatanResponse.diskripsi_kegiatan,
                            kegiatanResponse.gambar_kegiatan,
                            kegiatanResponse.jam_kegiatan,
                            kegiatanResponse.tgl_kegiatan
                        )
                    )
                }
                emit(BaseResult.Success(kegiatan))
            } else {
                val type = object : TypeToken<WrappedListResponse<KegiatanResponse>>() {}.type
                val err = Gson().fromJson<WrappedListResponse<KegiatanResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                err.code = response.code()
                emit(BaseResult.Error(err))
                Log.d("searchKegiatan", err.toString())
            }
        }

    }

    override suspend fun setHistoryKegiatan(historyKegiatanRequest: HistoryKegiatanRequest): Flow<BaseResult<HistoryKegiatanEntity, WrappedResponse<HistoryPencarianResponse>>> {
        return flow {
            val response = kegiatanApi.setHistoryKegiatan(historyKegiatanRequest)
            if (response.isSuccessful) {
                val body = response.body()!!
                val historyKegiatanEntity = HistoryKegiatanEntity(
                    body.data?.id!!,
                    body.data?.judul!!,
                    body.data?.waktu!!
                )

                emit(BaseResult.Success(historyKegiatanEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<HistoryPencarianResponse>>() {}.type
                val err: WrappedResponse<HistoryPencarianResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                err.code = response.code()
                emit(BaseResult.Error(err))

            }
        }
    }


}