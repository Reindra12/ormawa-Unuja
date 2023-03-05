package com.reindrairawan.organisasimahasiswa.data.account.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.data.account.remote.api.AccountApi
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.domain.account.AccountRepository
import com.reindrairawan.organisasimahasiswa.domain.account.entity.AccountEntity
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountApi : AccountApi) :
    AccountRepository {
    override suspend fun updateAccount(
        updateTokenRequest: UpdateTokenRequest,
        id: String
    ): Flow<BaseResult<AccountEntity, WrappedResponse<UpdateTokenResponse>>> {
      return flow {
          val response = accountApi.updateFcmToken(updateTokenRequest, id)
          if (response.isSuccessful){
              val body = response.body()!!
              val account = AccountEntity(body.data!!.id_mahasiswa, body.data!!.fcmToken, body.data!!.nama)
              emit(BaseResult.Success(account))
          }else{
              val type = object : TypeToken<WrappedResponse<UpdateTokenResponse>>(){}.type
              val err = Gson().fromJson<WrappedResponse<UpdateTokenResponse>>(response.errorBody()!!.charStream(), type)
              err.code = response.code()
              emit(BaseResult.Error(err))
          }
      }
    }
}