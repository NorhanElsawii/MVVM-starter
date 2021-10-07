package com.starter.mvvm.ui.main.paging

import androidx.lifecycle.MutableLiveData
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.ui.main.MainRepository
import com.starter.mvvm.utils.Constants.FIRST_PAGE
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseDataSource

class MainDataSource(
    private val mainRepository: MainRepository,
    private val status: MutableLiveData<Status>,
) :
    BaseDataSource<User>(mainRepository, status) {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>,
    ) {
        subscribe<List<User>, Any>(
            mainRepository.getUsers(FIRST_PAGE),
            { loadInitial(params, callback) },
            {
                callback.onResult(it ?: ArrayList(), null, FIRST_PAGE + 1)
            },
            false
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        subscribe<List<User>, Any>(
            mainRepository.getUsers(params.key.toInt()),
            { loadAfter(params, callback) },
            {
                callback.onResult(it ?: ArrayList(), params.key.toInt() + 1)
            },
            true
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        //pass
    }
}