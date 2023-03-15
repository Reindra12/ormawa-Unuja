package com.reindrairawan.organisasimahasiswa.presentation.dashboard.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.usecase.GetAllKegiatanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KegiatanViewModel @Inject constructor(private val getAllKegiatanUseCase: GetAllKegiatanUseCase) :
    ViewModel() {
    private val state = MutableStateFlow<KegiatanState>(KegiatanState.Init)
    val mState: StateFlow<KegiatanState> get() = state

    private val kegiatan = MutableStateFlow<List<KegiatanEntity>>(mutableListOf())
    val mKegiatan: StateFlow<List<KegiatanEntity>> get() = kegiatan

     fun fetchAllKegiatan() {
        viewModelScope.launch {
            getAllKegiatanUseCase.invoke()
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
                            kegiatan.value = result.data
                            Log.d("TAG", "TAG: " + result.data)
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                            Log.d("TAG", "TAG: " + result.rawResponse.message)
                        }
                    }
                }
        }
    }



    private fun showToast(message: String) {
        state.value = KegiatanState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = KegiatanState.IsLoading(false)

    }

    private fun setLoading() {
        state.value = KegiatanState.IsLoading(true)
    }


}

sealed class KegiatanState {
    object Init : KegiatanState()
    data class IsLoading(val isLoading: Boolean) : KegiatanState()
    data class ShowToast(val message: String) : KegiatanState()

}
