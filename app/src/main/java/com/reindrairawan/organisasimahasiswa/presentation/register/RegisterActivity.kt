package com.reindrairawan.organisasimahasiswa.presentation.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun signUp() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validate(name, email, password)) {

            }
        }
    }

    private fun validate(name: String, email: String, password: String): Boolean {
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