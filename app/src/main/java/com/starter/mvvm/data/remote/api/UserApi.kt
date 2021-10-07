package com.starter.mvvm.data.remote.api

import com.starter.mvvm.data.remote.ApiUrls.GET_USER
import com.starter.mvvm.data.remote.ApiUrls.GET_USERS
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.RemoteKeys.ID
import com.starter.mvvm.data.remote.RemoteKeys.PAGE
import com.starter.mvvm.data.remote.entites.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET(GET_USERS)
    fun getUserList(@Query(PAGE) page: Int): Single<BaseResponse<List<User>>>

    @GET(GET_USER)
    fun getUser(@Path(ID) id: Int): Single<BaseResponse<User>>
}
