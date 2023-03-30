package com.reindrairawan.organisasimahasiswa.presentation.dashboard.kegiatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.reindrairawan.organisasimahasiswa.R
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.reindrairawan.organisasimahasiswa.databinding.ActivityKegiatanBinding
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.gone
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.visible
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.kegiatan.DetailKegiatan.DetailKegiatanActivity
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.search.GetKegiatanAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.checkerframework.common.returnsreceiver.qual.This

@AndroidEntryPoint
class KegiatanActivity : AppCompatActivity() {

    private val viewModel: KegiatanByIdJenisViewModel by viewModels()
    private var idJenisKegiatan: Int = 0
    private val binding: ActivityKegiatanBinding by lazy {
        ActivityKegiatanBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var kegiatanAdapter: GetKegiatanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        idJenisKegiatan = bundle?.get("id") as Int

        actionView()
        observeKegiatan()
        setRecyclerview()
    }

    private fun setRecyclerview() {

        kegiatanAdapter = GetKegiatanAdapter(this, mutableListOf())
        kegiatanAdapter.setItemTapListener(object : GetKegiatanAdapter.OnItemTap {
            override fun onTap(kegiatans: KegiatanEntity) {
                val b = contentValuesOf("id" to kegiatans.id)
                val intent = Intent(this@KegiatanActivity, DetailKegiatanActivity::class.java)
                intent.putExtra("id", kegiatans.id)
                startActivity(intent)


                Toast.makeText(
                    this@KegiatanActivity,
                    "" + b,
                    Toast.LENGTH_SHORT
                ).show()


            }
        })

        binding.rvKegiatan.apply {
            binding.rvKegiatan.visible()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = kegiatanAdapter
        }
    }

    private fun observeKegiatan() {
        observeState()
        observeKegiatanById()

    }

    private fun observeKegiatanById() {
        viewModel.mKegiatan.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { kegiatan ->
                handleKegiatan(kegiatan)
            }.launchIn(lifecycleScope)
    }

    private fun handleKegiatan(kegiatan: List<KegiatanEntity>) {
        binding.rvKegiatan.adapter.let {
            if (it is GetKegiatanAdapter) {
                viewModel.fetchKegiatanById(idJenisKegiatan)
                it.setData(kegiatan)
            }
        }
    }

    private fun observeState() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach { state ->
            handleState(state)
        }.launchIn(lifecycleScope)
    }

    private fun handleState(state: KegiatanByIdJenisState) {
        when (state) {
            is KegiatanByIdJenisState.Init -> Unit
            is KegiatanByIdJenisState.ShowToast -> showToast(state.message)
            is KegiatanByIdJenisState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
//            binding.progressbar.visible()
        } else {
            binding.loadingProgressBar.gone()

        }
    }

    private fun actionView() {
        binding.imgBack.setOnClickListener {
            finish()
        }

    }
}