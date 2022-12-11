package com.reindrairawan.organisasimahasiswa.domain.login.entity

data class LoginEntity(
    var id: Int,
    var nama: String,
    var nim: String,
    var user: String,
    var token: String
)