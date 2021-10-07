package com.starter.mvvm.ui.details

import com.starter.mvvm.data.remote.apicalls.UserApiCall
import com.starter.mvvm.utils.base.BaseRepository
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val userApiCall: UserApiCall) :
    BaseRepository() {

    fun getUser(id: Int) = userApiCall.getUser(id)
}