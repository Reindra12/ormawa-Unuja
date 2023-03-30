package com.reindrairawan.organisasimahasiswa.presentation.dashboard.kegiatan.DetailKegiatan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.DaftarKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.DaftarKegiatanResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.KegiatanResponse
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.DaftarKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase.DaftarKegiatanUseCase
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase.DetailKegiatanUseCase
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.kegiatan.KegiatanByIdJenisState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailKegiatanViewModel @Inject constructor(
    private val detailKegiatanUseCase: DetailKegiatanUseCase,
    private val daftarKegiatanUseCase: DaftarKegiatanUseCase
) :
    ViewModel() {
    private val state = MutableStateFlow<DetailKegiatanState>(DetailKegiatanState.Init)
    val mState: StateFlow<DetailKegiatanState> get() = state
    private val _detailKegiatan = MutableStateFlow<KegiatanEntity?>(null)
    val detailKegiatan: StateFlow<KegiatanEntity?> get() = _detailKegiatan

    fun detailKegiatan(id: Int) {
        viewModelScope.launch {
            detailKegiatanUseCase.invoke(id)
                .onStart { setLoading() }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            Log.d("DETAIL", "fetched: " + result.data)
                            _detailKegiatan.value = result.data
                        }
                        is BaseResult.Error -> {
//                            errorGetKegiatan(result.rawResponse)
                            Log.d("DETAIL", "fetched: " + result.rawResponse.message)
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

    fun daftarKegiatan(daftarKegiatanRequest: DaftarKegiatanRequest) {
        viewModelScope.launch {
            daftarKegiatanUseCase.invoke(daftarKegiatanRequest)
                .onStart { setLoading() }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                    Log.d("DAFTAR", "fetched:" + exception.message.toString())

                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            Log.d("DAFTAR", "fetched:" + result.data)
                            successDaftar(result.data)
                        }
                        is BaseResult.Error -> {
                            Log.d("DAFTAR", "fetched:" + result.rawResponse.message)
//                            showToast(result.rawResponse.message)
                            errorDaftar(result.rawResponse)
                        }

                    }
                }
        }
    }

    private fun errorDaftar(rawResponse: WrappedResponse<DaftarKegiatanResponse>) {
        state.value = DetailKegiatanState.ErrorDaftar(rawResponse)
    }

    private fun successDaftar(daftarKegiatanEntity: DaftarKegiatanEntity) {
        state.value = DetailKegiatanState.SuccessDaftar(daftarKegiatanEntity)
    }


    private fun showToast(message: String) {
        state.value = DetailKegiatanState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = DetailKegiatanState.IsLoading(false)
    }

    private fun setLoading() {
        state.value = DetailKegiatanState.IsLoading(true)
    }
}

sealed class DetailKegiatanState {
    object Init : DetailKegiatanState()
    data class IsLoading(val isLoading: Boolean) : DetailKegiatanState()
    data class ShowToast(val message: String) : DetailKegiatanState()

    data class SuccessDaftar(val daftarKegiatanEntity: DaftarKegiatanEntity) : DetailKegiatanState()

    data class ErrorDaftar(val rawResponse: WrappedResponse<DaftarKegiatanResponse>) :
        DetailKegiatanState()

}
