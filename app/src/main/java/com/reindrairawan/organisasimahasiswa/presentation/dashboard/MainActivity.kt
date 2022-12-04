package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reindrairawan.organisasimahasiswa.R

import com.reindrairawan.organisasimahasiswa.databinding.ActivityMainBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.*
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.jenisKegiatan.ShowImageActivity
import com.reindrairawan.organisasimahasiswa.presentation.main.IntroActivity
import com.reindrairawan.organisasimahasiswa.utils.cameraX.CameraActivity
import com.reindrairawan.organisasimahasiswa.utils.cameraX.reduceFileImage
import com.reindrairawan.organisasimahasiswa.utils.cameraX.rotateBitmap
import com.reindrairawan.organisasimahasiswa.utils.cameraX.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import java.io.File

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = hostFragment.navController
        val navView = binding.bottomNavView
        navView.setupWithNavController(navController)

    }



}