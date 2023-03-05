package com.reindrairawan.organisasimahasiswa.data.account

import com.reindrairawan.organisasimahasiswa.data.account.remote.api.AccountApi
import com.reindrairawan.organisasimahasiswa.data.account.repository.AccountRepositoryImpl
import com.reindrairawan.organisasimahasiswa.data.common.module.NetworkModule
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.api.CategoriesApi
import com.reindrairawan.organisasimahasiswa.data.dashboard.repository.CategoriesRepositoryImpl
import com.reindrairawan.organisasimahasiswa.domain.account.AccountRepository
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.CategoriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class AccountModule {
    @Singleton
    @Provides
    fun provideAccountApi(retrofit: Retrofit): AccountApi {
        return retrofit.create(AccountApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAccountRepository(accountApi: AccountApi): AccountRepository {
        return AccountRepositoryImpl(accountApi)
    }

}