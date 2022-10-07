package com.reindrairawan.organisasimahasiswa.presentation

import android.app.Application

//import com.reindrairawan.organisasimahasiswa.presentation.login.LoginActivity2
import dagger.hilt.android.HiltAndroidApp

//@HiltAndroidApp(LoginActivity2::class)
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
