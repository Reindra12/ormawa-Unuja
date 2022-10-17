package com.reindrairawan.organisasimahasiswa.data.fuzzy

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FuzzyEntity(


    var nama: String,
    var nilaiTinggi: String,
    var nilaiTurun: String


) : Parcelable
