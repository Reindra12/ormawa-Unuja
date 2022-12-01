package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.awesomedialog.AwesomeDialog
import com.reindrairawan.organisasimahasiswa.domain.common.base.BaseResult
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.usecase.CategoriesUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(val categoriesUseCase: CategoriesUseCase) :
    ViewModel() {
    private val state = MutableStateFlow<DashboardState>(DashboardState.Init)
    val mState: StateFlow<DashboardState> get() = state

    private val categories = MutableStateFlow<List<CategoriesEntity>>(mutableListOf())
    val mCategories: StateFlow<List<CategoriesEntity>> get() = categories

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            categoriesUseCase.invoke()
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
                            categories.value = result.data
                            Log.d("TAG", "TAG: "+result.data)

                        }
                        is BaseResult.Error -> {
                            Log.d("TAG", "TAG: "+result.rawResponse)
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }



    private fun showToast(message: String) {
        state.value = DashboardState.ShowToast(message)
    }

    private fun hideLoading() {
        state.value = DashboardState.IsLoading(false)
    }

    private fun setLoading() {
        state.value = DashboardState.IsLoading(true)
    }
}

sealed class DashboardState {
    object Init : DashboardState()
    data class IsLoading(val isLoading: Boolean) : DashboardState()
    data class ShowToast(val message: String) : DashboardState()

}
