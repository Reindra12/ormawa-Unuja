package com.reindrairawan.organisasimahasiswa.presentation.dashboard.kegiatan.DetailKegiatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.reindrairawan.organisasimahasiswa.BuildConfig
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.DaftarKegiatanRequest
import com.reindrairawan.organisasimahasiswa.data.kegiatan.remote.dto.DaftarKegiatanResponse
import com.reindrairawan.organisasimahasiswa.databinding.ActivityDetailBinding
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.DaftarKegiatanEntity
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.gone
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailKegiatanActivity : AppCompatActivity() {
    private val viewModel: DetailKegiatanViewModel by viewModels()
    var id: Int = 0

    @Inject
    lateinit var pref: SharedPrefs

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        setContentView(binding.root)
        observe()
        fetchDetailKegiatan()
        actionView()
        daftarKegiatan()
    }

    private fun daftarKegiatan() {
        binding.daftarkegiatanTv.setOnClickListener {
            viewModel.daftarKegiatan(
                DaftarKegiatanRequest(

                    pref.getIdMahasiswa(),
                    id,
                    "T"
                )
            )

        }
    }


    private fun fetchDetailKegiatan() {
        val intent = getIntent().getIntExtra("id", 0)
        if (intent != 0) {
            id = intent
            viewModel.detailKegiatan(intent)
        }
    }

    private fun observe() {
        observeState()
        observeKegiatan()
    }

    private fun observeKegiatan() {
        viewModel.detailKegiatan.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { kegiatan ->
                kegiatan?.let {
                    handleKegiatan(it)

                }
            }
            .launchIn(lifecycleScope)
    }

    private fun handleKegiatan(kegiatanEntity: KegiatanEntity) {
        binding.judulTv.setText(kegiatanEntity.nama_kegiatan)
        binding.diskripsiKegiatanTv.setText(kegiatanEntity.diskripsi_kegiatan)
        binding.hariKegiatanTv.setText(kegiatanEntity.hari)
        binding.tempatKegiatanTv.setText(kegiatanEntity.tempat)
        binding.tglKegiatanTv.setText(kegiatanEntity.tgl_kegiatan)
        Glide.with(this)
            .load(BuildConfig.BASE_ASSETS + "kegiatan/" + kegiatanEntity.gambar_kegiatan)
            .into(binding.ditailkegiatanImg)
    }

    private fun observeState() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleDetailKegiatan(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleDetailKegiatan(state: DetailKegiatanState) {
        when (state) {
            is DetailKegiatanState.Init -> Unit
            is DetailKegiatanState.IsLoading -> handleLoading(state.isLoading)
            is DetailKegiatanState.ShowToast -> showToast(state.message)
            is DetailKegiatanState.SuccessDaftar -> handleSuksesDaftar(state.daftarKegiatanEntity)
            is DetailKegiatanState.ErrorDaftar -> handleGagalDaftar(state.rawResponse)
        }
    }

    private fun handleGagalDaftar(rawResponse: WrappedResponse<DaftarKegiatanResponse>) {
        showToast(rawResponse.message)
    }

    private fun handleSuksesDaftar(daftarKegiatanEntity: DaftarKegiatanEntity) {

    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.loadingProgressBar.visible()
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