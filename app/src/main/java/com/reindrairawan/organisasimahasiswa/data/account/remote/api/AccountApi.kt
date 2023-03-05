package com.reindrairawan.organisasimahasiswa.data.account.remote.api

import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterRequest
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApi {
    @POST("mahasiswa/{id}")
    suspend fun updateFcmToken(@Body updateTokenRequest: UpdateTokenRequest, @Path("id") id:String): Response<WrappedResponse<UpdateTokenResponse>>
}