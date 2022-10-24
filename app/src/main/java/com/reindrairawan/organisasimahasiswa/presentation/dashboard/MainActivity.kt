package com.reindrairawan.organisasimahasiswa.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    private fun checkIsLoggedIn() {
        if (prefs.getToken().isEmpty()) {
            goToLoginActivity()
        }
    }


    private fun goToLoginActivity() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        checkIsLoggedIn()
    }
}