package com.reindrairawan.organisasimahasiswa.presentation.fuzzy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrediksiViewModel : ViewModel() {

    val name = MutableLiveData<String>()
    val nilaitinggi = MutableLiveData<String>()
    val nilaiturun = MutableLiveData<String>()

}