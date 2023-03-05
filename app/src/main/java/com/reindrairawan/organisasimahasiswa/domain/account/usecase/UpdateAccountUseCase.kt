package com.reindrairawan.organisasimahasiswa.domain.account.usecase

import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenResponse
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.domain.account.AccountRepository
import com.reindrairawan.organisasimahasiswa.domain.account.entity.AccountEntity
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {
    suspend fun invoke(updateTokenRequest: UpdateTokenRequest, id : String): Flow<BaseResult<AccountEntity, WrappedResponse<UpdateTokenResponse>>> {
      return accountRepository.updateAccount(updateTokenRequest, id)
    }
}