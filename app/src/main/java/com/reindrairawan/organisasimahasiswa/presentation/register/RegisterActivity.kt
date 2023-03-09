package com.reindrairawan.organisasimahasiswa.presentation.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.common.utils.WrappedResponse
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterRequest
import com.reindrairawan.organisasimahasiswa.data.register.remote.dto.RegisterResponse
import com.reindrairawan.organisasimahasiswa.databinding.ActivityRegisterBinding
import com.reindrairawan.organisasimahasiswa.domain.register.entity.RegisterEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showGenericAlertDialog
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var prefs: SharedPrefs
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        goBack()
        signUp()
        observe()


    }

    private fun goBack() {
        binding.backButton.setOnClickListener { finish() }
    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: RegisterViewModel.RegisterActivityState) {
        when (state) {
            is RegisterViewModel.RegisterActivityState.IsLoading -> handleLoading(state.isLoading)
            is RegisterViewModel.RegisterActivityState.Init -> Unit
            is RegisterViewModel.RegisterActivityState.SuccessRegister -> handleSuccessRegister(
                state.registerEntity)
            is RegisterViewModel.RegisterActivityState.ShowToast -> showToast(state.message)
            is RegisterViewModel.RegisterActivityState.ErrorRegister -> handleErrorRegister(state.rawResponse)
        }
    }

    private fun handleErrorRegister(rawResponse: WrappedResponse<RegisterResponse>) {
        showGenericAlertDialog(rawResponse.errors)

    }

    private fun handleSuccessRegister(registerEntity: RegisterEntity) {
        prefs.saveToken(registerEntity.token)
        setResult(RESULT_OK)
        finish()
    }

    private fun handleLoading(loading: Boolean) {
        binding.loadingProgressBar.isIndeterminate = loading
        binding.registerButton.isEnabled = !loading
        binding.backButton.isEnabled = !loading
        if (!loading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun signUp() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val password_confirm = binding.passwordConfirmEditText.text.toString().trim()

            if (validate(name, email, password, password_confirm)) {

                viewModel.register(RegisterRequest(name, email, password, password_confirm))
            }
        }
    }

    private fun validate(
        name: String,
        email: String,
        password: String,
        password_confirm: String
    ): Boolean {
        resetAllError()
        if (name.isEmpty()) {
            setNameError(getString(R.string.error_name_not_valid))
            return false
        }
        if (email.isEmpty()) {
            setEmailError(getString(R.string.error_email_not_valid))
            return false

        }
        if (password.length < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }
        if (password_confirm.length < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }
        return true
    }

    private fun setNameError(e: String?) {
        binding.nameInput.error = e

    }

    private fun setEmailError(e: String?) {
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?) {
        binding.passwordInput.error = e
    }

    private fun resetAllError() {
        setNameError(null)
        setPasswordError(null)
        setEmailError(null)
    }
}