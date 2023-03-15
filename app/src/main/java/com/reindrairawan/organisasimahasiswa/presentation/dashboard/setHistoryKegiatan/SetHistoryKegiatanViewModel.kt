package com.reindrairawan.organisasimahasiswa.presentation.dashboard.setHistoryKegiatan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.HistoryKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase.SetHistoryKegiatanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetHistoryKegiatanViewModel @Inject constructor(private val setHistoryKegiatanUseCase: SetHistoryKegiatanUseCase) :
    ViewModel() {
    private val state = MutableStateFlow<SetHistoryKegiatanState>(SetHistoryKegiatanState.Init)
    val mState: StateFlow<SetHistoryKegiatanState> get() = state

    fun setHistory(historyKegiatanRequest: HistoryKegiatanRequest) {
        viewModelScope.launch {
            setHistoryKegiatanUseCase.invoke(historyKegiatanRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            successSetHistory(result.data)
                        }
                        is BaseResult.Error -> {
                            errorSetHistory(result.rawResponse)
                        }
                    }
                }
        }
    }

    private fun errorSetHistory(rawResponse: WrappedResponse<HistoryPencarianResponse>) {
        state.value = SetHistoryKegiatanState.ErrorSetHistory(rawResponse)
    }

    private fun successSetHistory(data: HistoryKegiatanEntity) {
        state.value = SetHistoryKegiatanState.SuccessSetHistory(data)

    }

    private fun showToast(message: String) {
        state.value = SetHistoryKegiatanState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = SetHistoryKegiatanState.IsLoading(false)
    }

    private fun setLoading() {
        state.value = SetHistoryKegiatanState.IsLoading(true)
    }

    sealed class SetHistoryKegiatanState {
        object Init : SetHistoryKegiatanState()
        data class IsLoading(val isLoading: Boolean) : SetHistoryKegiatanState()
        data class ShowToast(val message: String) : SetHistoryKegiatanState()
        data class SuccessSetHistory(val historyKegiatanEntity: HistoryKegiatanEntity) :
            SetHistoryKegiatanState()

        data class ErrorSetHistory(val rawResponse: WrappedResponse<HistoryPencarianResponse>) :
            SetHistoryKegiatanState()
    }
}

