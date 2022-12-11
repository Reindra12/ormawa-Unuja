package com.reindrairawan.organisasimahasiswa.presentation.dashboard.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.usecase.SearchKegiatanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKegiatanViewModel @Inject constructor(
    private val searchKegiatanUseCase: SearchKegiatanUseCase
) : ViewModel() {
    private val state = MutableStateFlow<SearchKegiatanState>(SearchKegiatanState.Init)
    val mState: StateFlow<SearchKegiatanState> get() = state
    private val kegiatan = MutableStateFlow<List<KegiatanEntity>>(mutableListOf())
    val mKegiatan: StateFlow<List<KegiatanEntity>> get() = kegiatan

//    init {
//        fetchSearchKegiatan()
//    }

     fun fetchSearchKegiatan(nama: String) {
        viewModelScope.launch {
            searchKegiatanUseCase.invoke(nama)
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
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }

        }
    }

    private fun showToast(message: String) {
        state.value = SearchKegiatanState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = SearchKegiatanState.IsLoading(false)
    }

    private fun setLoading() {
        state.value = SearchKegiatanState.IsLoading(true)
    }
}

sealed class SearchKegiatanState {
    object Init : SearchKegiatanState()
    data class IsLoading(val isLoading: Boolean) : SearchKegiatanState()
    data class ShowToast(val message: String) : SearchKegiatanState()
}
