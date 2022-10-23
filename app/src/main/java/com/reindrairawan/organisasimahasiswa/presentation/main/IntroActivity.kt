package com.reindrairawan.organisasimahasiswa.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.reindrairawan.organisasimahasiswa.databinding.ActivityIntroBinding
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.login.LoginActivity
import com.reindrairawan.organisasimahasiswa.presentation.register.RegisterActivity

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    lateinit var prefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginConstraintLayout.setOnClickListener {
            goToSignIn()
        }

        binding.signupConstrainlayout.setOnClickListener {
            goToSignUp()
        }
    }

    private fun goToSignUp() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun goToSignIn() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}