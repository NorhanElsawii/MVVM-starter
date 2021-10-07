package com.starter.mvvm.utils.base

import androidx.paging.DataSource

/**
 * Created by Norhan Elsawi on 10/06/2021.
 */
abstract class BaseDataSourceFactory<I, DS : BaseDataSource<I>> :
    DataSource.Factory<Int, I>() {

    private var dataSource: DS? = null

    abstract fun getDataSource(): DS

    override fun create(): DataSource<Int, I> {
        dataSource = getDataSource()
        return requireNotNull(dataSource)
    }

    fun retry() {
        dataSource?.retry()
    }

    fun refresh(isForce: Boolean = false) {
        dataSource?.invalidate(isForce)
    }

    fun onCleared() {
        dataSource?.onCleared()
    }
}