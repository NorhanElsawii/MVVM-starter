package com.starter.mvvm.ui.main.paging

import androidx.lifecycle.MutableLiveData
import com.starter.mvvm.data.local.entities.ListAndFirstUser
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.ui.main.MainRepository
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseDataSourceFactory

class MainDataSourceFactory(
    private val repository: MainRepository,
    private val status: MutableLiveData<Status<BaseResponse<ListAndFirstUser>, Any>>,
) : BaseDataSourceFactory<User, BaseResponse<ListAndFirstUser>, Any, MainDataSource>() {

    override fun getDataSource(): MainDataSource {
        return MainDataSource(repository, status)
    }
}
