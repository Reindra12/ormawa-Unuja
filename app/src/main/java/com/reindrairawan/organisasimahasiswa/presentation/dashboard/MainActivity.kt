package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.reindrairawan.organisasimahasiswa.R

import com.reindrairawan.organisasimahasiswa.databinding.ActivityMainBinding
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.*
import com.reindrairawan.organisasimahasiswa.presentation.scanner.ScanFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var codeScanner: CodeScanner
    private var doubleBackToExitPressedOnce = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        openScanning()

        val hostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = hostFragment.navController
        val navView = binding.bottomNavView
        navView.setupWithNavController(navController)

        binding.scanFloat.setOnClickListener {
            codeScanner.startPreview()
            scanner(true)
        }

    }

    private fun openScanning() {
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                scanner(false)
                codeScanner.stopPreview()
                showToast("hasil scan :" + it.text)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                showToast("camera error : "+it.message.toString())
            }
        }
    }

    private fun scanner(status: Boolean) {
        if (status) {
            binding.scannerView.visibility = View.VISIBLE
            binding.coordinator.visibility = View.GONE
        } else {
            binding.scannerView.visibility = View.GONE
            binding.coordinator.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        binding.scannerView.gone()
        binding.coordinator.visible()

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan kembali sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}

