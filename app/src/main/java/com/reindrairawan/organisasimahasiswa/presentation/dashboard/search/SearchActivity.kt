package com.reindrairawan.organisasimahasiswa.presentation.dashboard.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dealjava.dealjava.ui.home.search.RecentlyAdapter
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.DummyData
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.searchview.remote.dto.HistoryPencarianResponse
import com.reindrairawan.organisasimahasiswa.databinding.ActivitySearchBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.entity.RecentlyEventEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.HistoryKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.gone
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.visible
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.setHistoryKegiatan.SetHistoryKegiatanViewModel
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.setHistoryKegiatan.SetHistoryKegiatanViewModel.SetHistoryKegiatanState

import com.reindrairawan.organisasimahasiswa.utils.TinyDB
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: KegiatanViewModel by viewModels()
    private val viewModelhistory: SearchKegiatanViewModel by viewModels()
    private val viewModelSetHistory: SetHistoryKegiatanViewModel by viewModels()

    private lateinit var recentlyAdapter: RecentlyAdapter
    private lateinit var getKegiatanAdapter: GetKegiatanAdapter
    var recentlyEventEntity: ArrayList<RecentlyEventEntity> = arrayListOf()

    @Inject
    lateinit var prefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        setContentView(binding.root)


        viewModel.fetchAllKegiatan()
        viewModelhistory.fetchSearchKegiatan(1)
        observe()
        observeHistory()
        observeSetHistory()
        setUpRecyclerView()
        actionView()
    }

    private fun observeSetHistory() {
        viewModelSetHistory.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateSethistory(state) }
    }

    private fun handleStateSethistory(state: SetHistoryKegiatanState) {
        when (state) {
            is SetHistoryKegiatanState.IsLoading -> handleLoading(state.isLoading)
            is SetHistoryKegiatanState.Init -> Unit
            is SetHistoryKegiatanState.SuccessSetHistory -> handleSuccessSetHistory(state.historyKegiatanEntity)
            is SetHistoryKegiatanState.ShowToast -> showToast(state.message)
            is SetHistoryKegiatanState.ErrorSetHistory -> handleErrorSetHistory(state.rawResponse)

        }
    }

    private fun handleErrorSetHistory(rawResponse: WrappedResponse<HistoryPencarianResponse>) {
        showToast(rawResponse.message)
    }

    private fun handleSuccessSetHistory(historyKegiatanEntity: HistoryKegiatanEntity) {

    }

    private fun observeHistory() {
        observeStateHistory()
        observeHistoryPencarian()
    }

    private fun observeHistoryPencarian() {
        viewModelhistory.mHistory.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { history ->
                handleHistory(history)
            }.launchIn(lifecycleScope)
    }

    private fun handleHistory(history: List<HistoryKegiatanEntity>) {
        history.forEach {
            binding.chipHistory.addView(createTagChip(this, it))
        }
    }

    private fun observeStateHistory() {
        viewModelhistory.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleStateHistory(state)
            }.launchIn(lifecycleScope)
    }

    private fun handleStateHistory(state: SearchKegiatanState) {
        when (state) {
            is SearchKegiatanState.IsLoading -> handleLoading(state.isLoading)
            is SearchKegiatanState.ShowToast -> showToast(state.message)
            is SearchKegiatanState.Init -> Unit
        }
    }

    private fun actionView() {
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                getKegiatanAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getKegiatanAdapter.filter.filter(newText)
                Log.d("onQueryTextSubmit2: ", newText!!)
                return false
            }

        })

    }

    private fun observe() {
        observeState()
        observeKegiatan()
    }

    private fun observeKegiatan() {
        viewModel.mKegiatan.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { kegiatan ->
                handleKegiatan(kegiatan)
            }.launchIn(lifecycleScope)
    }

    private fun handleKegiatan(kegiatan: List<KegiatanEntity>) {
        binding.rvRecently.adapter.let {
            if (it is GetKegiatanAdapter) {
                viewModel.fetchAllKegiatan()
                it.setData(kegiatan)
            }
        }
    }

    private fun observeState() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach { state ->
            handleState(state)
        }.launchIn(lifecycleScope)
    }

    private fun handleState(state: KegiatanState) {
        when (state) {
            is KegiatanState.IsLoading -> handleLoading(state.isLoading)
            is KegiatanState.ShowToast -> showToast(state.message)
            is KegiatanState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgressBar.visible()
        } else {
            binding.loadingProgressBar.gone()
        }
    }

    private fun setUpRecyclerView() {
        getKegiatanAdapter = GetKegiatanAdapter(this, mutableListOf())
        getKegiatanAdapter.setItemTapListener(object : GetKegiatanAdapter.OnItemTap {
            override fun onTap(kegiatans: KegiatanEntity) {

                val b = bundleOf("nama_kegiatan" to kegiatans.nama_kegiatan)
                recentlyEventEntity.add(RecentlyEventEntity(kegiatans.nama_kegiatan))
                viewModelSetHistory.setHistory(
                    HistoryKegiatanRequest(
                        kegiatans.nama_kegiatan,
                        "2020-12-12",
                        1
                    )
                )
            }

        })
        binding.rvRecently.apply {
            binding.rvRecently.visible()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = getKegiatanAdapter
        }
    }

    private fun createTagChip(context: Context, chipName: HistoryKegiatanEntity): Chip {
        return Chip(context).apply {
            text = chipName.judul
            setChipBackgroundColorResource(R.color.color_grey_light)
            setTextColor(ContextCompat.getColor(context, R.color.relative))
        }
    }

}