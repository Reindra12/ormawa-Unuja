package com.reindrairawan.organisasimahasiswa.domain.dashboard.category.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedListResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.CategoriesResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.CategoriesRepository
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {
    suspend fun invoke() : Flow<BaseResult<List<CategoriesEntity>, WrappedListResponse<CategoriesResponse>>>{
        return categoriesRepository.getCategories()
    }
}