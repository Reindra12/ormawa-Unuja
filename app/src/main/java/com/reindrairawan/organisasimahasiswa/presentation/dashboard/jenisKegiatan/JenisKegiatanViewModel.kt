package com.reindrairawan.organisasimahasiswa.presentation.dashboard.jenisKegiatan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.entity.JenisKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.usecase.JenisKegiatanUseCase
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.DashboardState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class JenisKegiatanViewModel @Inject constructor(private val jenisKegiatanUseCase: JenisKegiatanUseCase) :
    ViewModel() {
    private val state = MutableStateFlow<JenisKegiatanState>(JenisKegiatanState.Init)
    val mState: StateFlow<JenisKegiatanState> get() = state


    fun createJenisKegiatan(file: MultipartBody.Part, name: RequestBody) {
        viewModelScope.launch {
            jenisKegiatanUseCase.invoke(file, name)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.stackTraceToString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> state.value =
                            JenisKegiatanState.SuccessCreate(result.data)
                        is BaseResult.Error -> JenisKegiatanState.ErrorCreate(result.rawResponse)

                    }
                }

        }
    }

    private fun showToast(message: String) {
        state.value = JenisKegiatanState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = JenisKegiatanState.IsLoading(false)
    }

    private fun setLoading() {
        state.value = JenisKegiatanState.IsLoading(true)
    }

//    private fun successCreate() {
//        state.value = JenisKegiatanState.SuccessCreate
//    }
}

sealed class JenisKegiatanState {
    object Init : JenisKegiatanState()
    data class SuccessCreate(val jenisKegiatanEntity: JenisKegiatanEntity) : JenisKegiatanState()
    data class IsLoading(val isLoading: Boolean) : JenisKegiatanState()
    data class ShowToast(val message: String) : JenisKegiatanState()
    data class ErrorCreate(val rawResponse: WrappedResponse<JenisKegiatanResponse>) :
        JenisKegiatanState()
}