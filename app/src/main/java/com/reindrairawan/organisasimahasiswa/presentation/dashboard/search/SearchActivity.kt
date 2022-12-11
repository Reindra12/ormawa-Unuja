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
import com.reindrairawan.organisasimahasiswa.databinding.ActivitySearchBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.entity.RecentlyEventEntity
import com.reindrairawan.organisasimahasiswa.domain.searchview.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.gone
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.visible
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
    private lateinit var recentlyAdapter: RecentlyAdapter
    private lateinit var getKegiatanAdapter: GetKegiatanAdapter

    private val dummyData = mutableListOf(
        DummyData("Breakfast Buffet at Hotel Grandhika Iskandaryah Jakarta"),
        DummyData("Breakfast Buffet at Hotel Grandhika Iskandaryah Jakarta"),
        DummyData("Breakfast Buffet at Hotel Grandhika Iskandaryah Jakarta")
    )
    lateinit var tinyDB: TinyDB
    var recently = ArrayList<String>()

    var recentlyEventEntity: ArrayList<RecentlyEventEntity> = arrayListOf()

    @Inject
    lateinit var prefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        setContentView(binding.root)

        tinyDB = TinyDB(applicationContext)

        viewModel.fetchAllKegiatan()
        observe()
        setUpRecyclerView()
        actionView()
//        setChip()
//        getList()
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

                setLists(recentlyEventEntity)


//                showToast(recentlyEventEntity.size.toString())
            }

        })
        binding.rvRecently.apply {
            binding.rvRecently.visible()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = getKegiatanAdapter
        }
    }

    fun setLists(list: ArrayList<RecentlyEventEntity>) {
        val gson = Gson()
        val json = gson.toJson(list)//converting list to Json
        prefs.saveRecently(json)


    }

    private fun createTagChip(context: Context, chipName: RecentlyEventEntity): Chip {
        return Chip(context).apply {
            text = chipName.nama_jenis_kegiatan
            setChipBackgroundColorResource(R.color.color_grey_light)
            setTextColor(ContextCompat.getColor(context, R.color.relative))
        }

    }

    fun getList(): ArrayList<RecentlyEventEntity> {
        val gson = Gson()
        var json = prefs.getRecently()
        val type = object :
            TypeToken<ArrayList<RecentlyEventEntity>>() {}.type//converting the json to list
        return gson.fromJson(json, type)//returning the list
    }

    private fun setChip() {
        getList().forEach {
            binding.chipHistory.addView(createTagChip(this, it))
            showToast(getList().size.toString())
        }
    }

}