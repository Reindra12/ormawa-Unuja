package com.reindrairawan.organisasimahasiswa.data.searchview

import com.reindrairawan.organisasimahasiswa.data.common.module.NetworkModule
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.api.KegiatanApi
import com.reindrairawan.organisasimahasiswa.data.searchview.repository.KegiatanRepositoryImpl
import com.reindrairawan.organisasimahasiswa.domain.searchview.KegiatanRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class KegiatanModule {
    @Singleton
    @Provides
    fun provideKegiatanApi(retforit: Retrofit): KegiatanApi {
        return retforit.create(KegiatanApi::class.java)
    }


    @Singleton
    @Provides
    fun provideKegiatanRepository(kegiatanApi: KegiatanApi): KegiatanRepository {
        return KegiatanRepositoryImpl(kegiatanApi)
    }
}