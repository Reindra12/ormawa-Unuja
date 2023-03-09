package com.reindrairawan.organisasimahasiswa.presentation.login

import android.Manifest
import android.content.Intent
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.inappmessaging.internal.Logging
import com.google.firebase.messaging.FirebaseMessaging
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.account.remote.dto.UpdateTokenRequest
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginRequest
import com.reindrairawan.organisasimahasiswa.data.login.remote.dto.LoginResponse
import com.reindrairawan.organisasimahasiswa.databinding.ActivityLoginBinding
import com.reindrairawan.organisasimahasiswa.domain.login.entity.LoginEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.isEmail
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showGenericAlertDialog
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
//import com.reindrairawan.organisasimahasiswa.presentation.fuzzy.Prediksi_Activity
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.MainActivity
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.account.AccountViewModel
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.account.AccountViewModelState
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()
    private val viewModelAccount: AccountViewModel by viewModels()
    private lateinit var token: String


    @Inject
    lateinit var pref: SharedPrefs

    private val openRegisterActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                goToMainActivity()
            }

        }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        login()
        observe()
        askNotificationPermission()
        getFCMToken()
        observeStateAccount()

    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }.launchIn(lifecycleScope)
    }

    private fun handleState(state: LoginActivityState) {
        when (state) {
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.IsLoading -> handleIsLoading(state.isLoading)
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
        }
    }

    private fun handleIsLoading(isLoading: Boolean) {
//        binding.registerButton.isEnabled = !isLoading
        binding.loginButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if (!isLoading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleErrorLogin(rawResponse: WrappedResponse<LoginResponse>) {
        showGenericAlertDialog(rawResponse.errors)
        Log.d("error", "handleErrorLogin: " + rawResponse.message)
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity) {
        pref.saveToken(loginEntity.token)
        pref.saveUsername(loginEntity.name)
        pref.saveIdMahasiswa(loginEntity.id)
        goToMainActivity()
        viewModelAccount.updateAccount(UpdateTokenRequest(token), loginEntity.id.toString())


    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }



    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validate(email, password)) {
                viewModel.login(LoginRequest(email, password))
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        resetAllerror()
        if (!email.isEmail()) {
            setEmailError(getString(R.string.email_is_not_valid))
            return false
        }
        if (password.length < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }
        return true
    }

    private fun resetAllerror() {
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setPasswordError(e: String?) {
        binding.emailInput.error = e
    }

    private fun setEmailError(e: String?) {
        binding.passwordInput.error = e

    }

    private fun observeStateAccount() {
        viewModelAccount.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }.launchIn(lifecycleScope)
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

            // FCM SDK (and your app) can post notifications.

        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(Logging.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result


            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(Logging.TAG, token)
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) else mutableStateOf(true)
            // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//            } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}