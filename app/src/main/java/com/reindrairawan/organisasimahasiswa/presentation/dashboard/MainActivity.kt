package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.inappmessaging.internal.Logging.TAG
import com.google.firebase.messaging.FirebaseMessaging
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest

import com.reindrairawan.organisasimahasiswa.databinding.ActivityMainBinding
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.*
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.account.AccountViewModel
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.account.AccountViewModelState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: AccountViewModel by viewModels()

    @Inject
    lateinit var  pref: SharedPrefs


    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        pref.getIdMahasiswa()
        Toast.makeText(this, ""+pref.getIdMahasiswa(), Toast.LENGTH_SHORT).show()
        val hostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = hostFragment.navController
        val navView = binding.bottomNavView
        navView.setupWithNavController(navController)

//        askNotificationPermission()
//        getFCMToken()
        observeState()

    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: AccountViewModelState) {
        when (state) {
            is AccountViewModelState.SusccessUpdate -> showToast("update coy")
            is AccountViewModelState.Init -> Unit
            is AccountViewModelState.IsLoading -> handleLoading(state.isLoading)
            is AccountViewModelState.ShowToast -> showToast(state.message)
        }
    }

    private fun handleLoading(loading: Boolean) {

    }

}

