package com.reindrairawan.organisasimahasiswa.presentation.dashboard.account

import android.accounts.Account
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest
import com.reindrairawan.organisasimahasiswa.domain.account.entity.AccountEntity
import com.reindrairawan.organisasimahasiswa.domain.account.usecase.UpdateAccountUseCase
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val updateAccountUseCase: UpdateAccountUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow<AccountViewModelState>(AccountViewModelState.Init)
    val state: StateFlow<AccountViewModelState> get() = _state
    private val _account = MutableStateFlow<AccountEntity?>(null)
    val account: StateFlow<AccountEntity?> get() = _account

    private fun setLoading() {
        _state.value = AccountViewModelState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = AccountViewModelState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _state.value = AccountViewModelState.ShowToast(message)
    }


    fun updateAccount (updateTokenRequest: UpdateTokenRequest, id:String){
        viewModelScope.launch {
            updateAccountUseCase.invoke(updateTokenRequest, id)
                .onStart {
                    setLoading()
                }
                .catch {
                exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }
                .collect{ result->
                    hideLoading()
                    when(result){
                        is BaseResult.Success->{
                            _state.value = AccountViewModelState.SusccessUpdate
                        }
                        is BaseResult.Error->{
                            Log.e("Account", result.rawResponse.errors!![0].toString())

                            showToast(result.rawResponse.message)
                        }

                    }
                }
        }
    }

}


sealed class AccountViewModelState {
    object Init : AccountViewModelState()
    object SusccessUpdate : AccountViewModelState()
    data class IsLoading(val isLoading: Boolean) : AccountViewModelState()
    data class ShowToast(val message: String) : AccountViewModelState()

}