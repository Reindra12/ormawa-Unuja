package com.reindrairawan.organisasimahasiswa.data.searchview.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.api.KegiatanApi
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.KegiatanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KegiatanRepositoryImpl @Inject constructor(private val kegiatanApi: KegiatanApi) :
    KegiatanRepository {
    override suspend fun searchKegiatan(nama: String): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>> {
        return flow {
            val response = kegiatanApi.searchKegiatan(nama)
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

    override suspend fun getAllKegiatan(): Flow<BaseResult<List<KegiatanEntity>, WrappedListResponse<KegiatanResponse>>> {
        return flow {
            val response = kegiatanApi.getKegiatan()
            if (response.isSuccessful){
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
            }else{
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


}