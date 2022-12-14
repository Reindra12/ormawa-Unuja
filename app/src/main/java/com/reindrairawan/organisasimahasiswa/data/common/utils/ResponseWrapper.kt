package com.reindrairawan.organisasimahasiswa.data.common.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    var code: Int,
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
//    @SerializedName("errors") var errors: List<String>? = null,
    @SerializedName("errors") var errors: String,
    @SerializedName("data") var data: List<T>? = null
)

data class WrappedResponse<T>(
    var code: Int,
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
//    @SerializedName("errors") var errors : T? = null,
    @SerializedName("errors") var errors: String,
    @SerializedName("data") var data: T? = null,
)