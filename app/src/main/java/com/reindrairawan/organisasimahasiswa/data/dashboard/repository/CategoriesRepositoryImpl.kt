package com.reindrairawan.organisasimahasiswa.data.dashboard.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.api.CategoriesApi
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.CategoriesResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.CategoriesRepository
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val categoriesApi: CategoriesApi) :
    CategoriesRepository {
    override suspend fun getCategories(): Flow<BaseResult<List<CategoriesEntity>, WrappedListResponse<CategoriesResponse>>> {
        return flow {
            val response = categoriesApi.getCategories()
            if (response.isSuccessful) {
                val body = response.body()!!
                val categories = mutableListOf<CategoriesEntity>()
                body.data?.forEach { categoriesResponse ->
                    categories.add(
                        CategoriesEntity(
                            categoriesResponse.id,
                            categoriesResponse.name,
                            categoriesResponse.path
                        )
                    )

                }
                emit(BaseResult.Success(categories))
            }else{
                val type = object : TypeToken<WrappedListResponse<CategoriesResponse>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<CategoriesResponse>>(response.errorBody()!!.charStream(), type)!!
                err.code = response.code()
                emit(BaseResult.Error(err))
                Log.e("RemoteDataSource", err.toString())
            }
        }
    }
}