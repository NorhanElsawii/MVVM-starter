package com.starter.mvvm.data.remote.apicalls

import com.starter.mvvm.data.remote.api.UserApi
import retrofit2.Retrofit
import javax.inject.Inject

class UserApiCall @Inject constructor(retrofit: Retrofit) {

    private val userApi = retrofit.create(UserApi::class.java)

    fun getUserList(page: Int) =
        userApi.getUserList(page)

    fun getUser(id: Int) =
        userApi.getUser(id)
}