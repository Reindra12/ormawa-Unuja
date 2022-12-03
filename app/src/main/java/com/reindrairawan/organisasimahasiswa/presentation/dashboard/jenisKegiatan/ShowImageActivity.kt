package com.reindrairawan.organisasimahasiswa.presentation.dashboard.jenisKegiatan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.dashboard.remote.dto.JenisKegiatanResponse
import com.reindrairawan.organisasimahasiswa.databinding.ActivityShowImageBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.jenisKegiatan.entity.JenisKegiatanEntity
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showGenericAlertDialog
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.MainActivity
import com.reindrairawan.organisasimahasiswa.utils.cameraX.bitmapToFile
import com.reindrairawan.organisasimahasiswa.utils.cameraX.reduceFileImage
import com.reindrairawan.organisasimahasiswa.utils.cameraX.rotateBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class ShowImageActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowImageBinding
    private val viewModel: JenisKegiatanViewModel by viewModels()
    private var getbitmap: Bitmap? = null
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowImageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        observe()
        getImage()
        uploadImage()

    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChage(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChage(state: JenisKegiatanState) {
        when (state) {
            is JenisKegiatanState.IsLoading -> handleLoading(state.isLoading)
            is JenisKegiatanState.SuccessCreate -> handleSuccess(state.jenisKegiatanEntity)
            is JenisKegiatanState.ShowToast -> handleToast(state.message)
            is JenisKegiatanState.ErrorCreate -> handleErrorCreate(state.rawResponse)
            is JenisKegiatanState.Init -> Unit
        }
    }

    private fun handleToast(message: String) {
        showToast(message)
        Log.d("TAG", "handleErrorCreate: " + message)
    }

    private fun handleErrorCreate(rawResponse: WrappedResponse<JenisKegiatanResponse>) {
        showGenericAlertDialog(rawResponse.message)

    }

    private fun handleSuccess(jenisKegiatanEntity: JenisKegiatanEntity) {
        goToMainActivity()

    }

    private fun handleLoading(loading: Boolean) {
        binding.loadingProgressBar.isIndeterminate = loading
        if (!loading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun uploadImage() {
        resetAllerror()
        binding.uploadButton.setOnClickListener {
            val keterangan = binding.keteranganEditText.text.toString().trim()
            if (validasiData(keterangan)) {
                if (keterangan.equals("")) {
                    setKeteranganError(getString(R.string.keterangan_error))

                } else {
                    uploadImageToServer(keterangan, getFile)
                }
            }
        }
    }

    private fun uploadImageToServer(keterangan: String, getFile: File?) {
        val description = keterangan.toRequestBody("text/plain".toMediaType())
        val requestImageFile = getFile?.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "gambar_jenis_kegiatan",
            getFile?.name,
            requestImageFile!!
        )
        viewModel.createJenisKegiatan(imageMultipart, description)


    }

    private fun resetAllerror() {
        setKeteranganError(null)
    }

    private fun setKeteranganError(e: String?) {
        binding.nameEditText.error = e
    }

    private fun validasiData(keterangan: String): Boolean {
        if (keterangan.equals("")) {
            binding.nameEditText.error
            return false
        }
        return true
    }

    private fun getImage() {
        var files = intent.getSerializableExtra("Bitmap") as File
        val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean
        var status = intent.getStringExtra("status")
        if (status.equals("camera")) {
            getbitmap = rotateBitmap(BitmapFactory.decodeFile(files.path), isBackCamera)
            getFile = bitmapToFile(this, getbitmap!!, "jenis_kegiatan")
            binding.jenisKegiatanImg.setImageBitmap(getbitmap)
        } else {
            getFile = files
            getbitmap = BitmapFactory.decodeFile(getFile!!.path)
            binding.jenisKegiatanImg.setImageBitmap(getbitmap)
        }


    }

    private fun goToMainActivity() {
        startActivity(Intent(this@ShowImageActivity, MainActivity::class.java))
        finish()
    }
}