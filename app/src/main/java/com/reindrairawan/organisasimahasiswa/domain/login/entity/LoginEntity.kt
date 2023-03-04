package com.reindrairawan.organisasimahasiswa.domain.login.entity

data class LoginEntity(
    var id: Int,
    var name: String,
    var user: String,
    var token: String
)