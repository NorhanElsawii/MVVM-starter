package com.starter.mvvm.data.remote.entites

data class User(
    var id: Int,
    var email: String,
    var first_name: String,
    var last_name: String,
    var avatar: String,
)

data class Support(
    var url: String,
    var text: String,
)
