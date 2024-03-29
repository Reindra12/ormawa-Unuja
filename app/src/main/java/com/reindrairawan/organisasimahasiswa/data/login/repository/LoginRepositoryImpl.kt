package com.reindrairawan.organisasimahasiswa.data.login.repository

import android.util.Log
import com.google.firebase.inappmessaging.internal.Logging.TAG
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.login.remote.api.LoginApi
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginRequest
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.login.LoginRepository
import com.reindrairawan.organisasimahasiswa.domain.login.entity.LoginEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if (response.isSuccessful) {
                val body = response.body()
                val loginEntity = LoginEntity(
                    body?.data?.id!!,
                    body.data?.name!!,
                    body.data?.user!!,
                    body.data?.token!!
                )
                Log.d(TAG, "login: "+ body.errors.toString())
                emit(BaseResult.Success(loginEntity))

            } else {
                val type = object : TypeToken<WrappedResponse<LoginResponse>>() {}.type
                val err: WrappedResponse<LoginResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}