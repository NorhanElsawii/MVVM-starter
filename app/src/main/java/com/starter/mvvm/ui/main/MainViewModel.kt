package com.starter.mvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.starter.mvvm.data.local.entities.ListAndFirstUser
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.ui.main.paging.MainDataSourceFactory
import com.starter.mvvm.utils.Constants.PAGE_SIZE
import com.starter.mvvm.utils.SingleLiveEvent
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) :
    BaseViewModel(mainRepository) {

    var pagedList: LiveData<PagedList<User>>? = null
    val status = SingleLiveEvent<Status<BaseResponse<ListAndFirstUser>, Any>>()
    private var factory: MainDataSourceFactory? = null

    fun initPagedList() {
        factory = MainDataSourceFactory(mainRepository, status)
        pagedList =
            LivePagedListBuilder(requireNotNull(factory), PAGE_SIZE).build()
    }

    fun refresh() {
        factory?.refresh()
    }

    fun retry() {
        factory?.retry()
    }

    override fun onCleared() {
        super.onCleared()
        factory?.onCleared()
    }
}
