package com.reindrairawan.organisasimahasiswa.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.reindrairawan.organisasimahasiswa.R
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
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var pref: SharedPrefs

    private val openRegisterActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                goToMainActivity()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        login()
        observe()

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
        pref.saveUsername(loginEntity.nama)

        goToMainActivity()

    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

//    private fun gotoRegisterActivity() {
//        binding.registerButton.setOnClickListener {
//            openRegisterActivity.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
//        }
//    }


    private fun login() {
        binding.loginButton.setOnClickListener {
            val username = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validate(username, password)) {
                viewModel.login(LoginRequest(username, password))
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
}