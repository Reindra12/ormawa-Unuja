package com.reindrairawan.organisasimahasiswa.domain.register

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterRequest
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.register.entity.RegisterEntity


interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest):kotlinx.coroutines.flow.Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>>
}