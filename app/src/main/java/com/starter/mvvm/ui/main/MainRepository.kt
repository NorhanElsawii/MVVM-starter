package com.starter.mvvm.ui.main

import com.starter.mvvm.data.remote.apicalls.UserApiCall
import com.starter.mvvm.utils.base.BaseRepository
import javax.inject.Inject

class MainRepository @Inject constructor(private val userApiCall: UserApiCall) : BaseRepository() {

    fun getUsers(page: Int) = userApiCall.getUserList(page)
}
