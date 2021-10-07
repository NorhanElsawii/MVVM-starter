package com.starter.mvvm.ui.details

import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.utils.SingleLiveEvent
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val detailsRepository: DetailsRepository) :
    BaseViewModel(detailsRepository) {

    val status = SingleLiveEvent<Status>()

    fun getUser(id: Int) {
        subscribe<User, Any>(detailsRepository.getUser(id), status)
    }
}