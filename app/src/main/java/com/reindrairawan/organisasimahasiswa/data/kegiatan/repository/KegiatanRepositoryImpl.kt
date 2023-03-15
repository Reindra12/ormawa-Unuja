package com.reindrairawan.organisasimahasiswa.data.kegiatan.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.api.KegiatanApi
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.KegiatanRepository
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.HistoryKegiatanEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KegiatanRepositoryImpl @Inject constructor(private val kegiatanApi: KegiatanApi) :
    KegiatanRepository {
    override suspend fun kegiatanByIdJenis(id: Int): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>> {
        return flow {
            val response = kegiatanApi.kegiatanByIdJenis(id)
            if (response.isSuccessful){
                val body = response.body()
                val kegiatan = mutableListOf<KegiatanEntity>()
                body!!.data?.forEach { fetchkegiatan->
                    kegiatan.add(
                        KegiatanEntity(
                            fetchkegiatan.id_kegiatan,
                            fetchkegiatan.nama_kegiatan,
                            fetchkegiatan.diskripsi_kegiatan,
                            fetchkegiatan.gambar_kegiatan,
                            fetchkegiatan.tgl_kegiatan,
                            fetchkegiatan.jam_kegiatan,
                            fetchkegiatan.tempat


                        )
                    )
                }
                emit(BaseResult.Success(kegiatan))

            }else{
                val type= object: TypeToken<WrappedListResponse<KegiatanResponse>>() {}.type
                val err = Gson().fromJson<WrappedListResponse<KegiatanResponse>>(
                    response.errorBody()!!.charStream(),type
                )!!
                err.code = response.code()
                emit(BaseResult.Error(err))
                Log.d("searchKegiatan", err.toString())

            }
        }
    }

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
                            kegiatanResponse.tgl_kegiatan,
                            kegiatanResponse.jam_kegiatan,
                            kegiatanResponse.tempat

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