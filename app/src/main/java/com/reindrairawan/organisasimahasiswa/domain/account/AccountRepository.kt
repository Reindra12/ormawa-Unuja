package com.reindrairawan.organisasimahasiswa.domain.account

import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.domain.account.entity.AccountEntity
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow



interface AccountRepository {
    suspend fun updateAccount(updateTokenRequest: UpdateTokenRequest, id:String) : Flow<BaseResult<AccountEntity, WrappedResponse<UpdateTokenResponse>>>
}