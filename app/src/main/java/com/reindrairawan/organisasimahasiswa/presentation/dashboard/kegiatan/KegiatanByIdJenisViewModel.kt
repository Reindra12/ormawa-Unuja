package com.reindrairawan.organisasimahasiswa.presentation.dashboard.kegiatan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase.KegiatanByIdJenisUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KegiatanByIdJenisViewModel @Inject constructor(private val kegiatanUseCase: KegiatanByIdJenisUseCase) :
    ViewModel() {
    private val state = MutableStateFlow<KegiatanByIdJenisState>(KegiatanByIdJenisState.Init)
    val mState: StateFlow<KegiatanByIdJenisState> get() = state
    private val kegiatan = MutableStateFlow<List<KegiatanEntity>>(mutableListOf())
    val mKegiatan: StateFlow<List<KegiatanEntity>> get() = kegiatan

    fun fetchKegiatanById(id: Int) {
        viewModelScope.launch {
            kegiatanUseCase.invoke(id)
                .onStart { setLoading() }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when (result) {
                        is BaseResult.Success -> {
                            kegiatan.value = result.data
                            Log.d("YOLO", "fetched: "+result.data)

                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }


    private fun showToast(message: String) {
        state.value = KegiatanByIdJenisState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = KegiatanByIdJenisState.IsLoading(false)
    }

    private fun setLoading() {
        state.value = KegiatanByIdJenisState.IsLoading(true)
    }
}

sealed class KegiatanByIdJenisState {
    object Init : KegiatanByIdJenisState()
    data class IsLoading(val isLoading: Boolean) : KegiatanByIdJenisState()
    data class ShowToast(val message: String) : KegiatanByIdJenisState()
}
