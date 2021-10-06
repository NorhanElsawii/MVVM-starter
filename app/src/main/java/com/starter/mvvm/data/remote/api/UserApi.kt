package com.starter.mvvm.data.remote.api

import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    fun getUserList(@Query("page") page: Int): Single<BaseResponse<List<User>>>
}
