package com.reindrairawan.organisasimahasiswa.data.dashboard

import com.reindrairawan.organisasimahasiswa.data.common.module.NetworkModule
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.api.CategoriesApi
import com.reindrairawan.organisasimahasiswa.data.dashboard.repository.CategoriesRepositoryImpl
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.CategoriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)

class CategoriesModule {
    @Singleton
    @Provides
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi{
        return retrofit.create(CategoriesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCategoriesRepository(categoriesApi: CategoriesApi):CategoriesRepository{
        return CategoriesRepositoryImpl(categoriesApi)
    }
}