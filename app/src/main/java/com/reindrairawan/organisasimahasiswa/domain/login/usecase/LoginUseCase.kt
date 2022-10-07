package com.reindrairawan.organisasimahasiswa.domain.login.usecase

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginRequest
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.login.LoginRepository
import com.reindrairawan.organisasimahasiswa.domain.login.entity.LoginEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return loginRepository.login(loginRequest)
    }
}