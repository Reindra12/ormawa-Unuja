package com.reindrairawan.organisasimahasiswa.data.register.remote.api

import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginRequest
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginResponse
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterRequest
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<WrappedResponse<RegisterResponse>>
}