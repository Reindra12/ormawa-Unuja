package com.reindrairawan.organisasimahasiswa.domain.dashboard.category

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.CategoriesResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    suspend fun getCategories(): Flow<BaseResult<List<CategoriesEntity>, WrappedListResponse<CategoriesResponse>>>
}