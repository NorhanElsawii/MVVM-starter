package com.starter.mvvm.utils.base

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.starter.mvvm.R
import com.starter.mvvm.data.remote.ErrorResponse
import com.starter.mvvm.utils.SingleLiveEvent
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.extensions.getErrorResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

/**
 * Created by Norhan Elsawi on 7/010/2021.
 */
abstract class BaseDataSource<I, D, E>(
    private val repository: BaseRepository,
    private var status: MutableLiveData<Status<D, E>>,
) :
    PageKeyedDataSource<Int, I>() {

    private val compositeDisposable = CompositeDisposable()
    private var retry: Action? = null

    fun subscribe(
        single: Single<D>,
        retryAction: Action?,
        callBack: (m: D?) -> Unit,
        isLoadMore: Boolean,
    ) {
        setRetry(retryAction)
        when {
            repository.isNetworkConnected() -> addSubscription(
                single
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        if (isLoadMore)
                            status.postValue(Status.LoadingMore)
                        else
                            status.postValue(Status.Loading)
                    }
                    .subscribe({ response ->
                        if (isLoadMore)
                            status.postValue(Status.SuccessLoadingMore)
                        else
                            status.postValue(Status.Success(response))
                        callBack(response)
                        setRetry(null)
                    }, { error ->
                        val errorResponse = error.getErrorResponse<E>()
                        if (errorResponse != null) {
                            if (isLoadMore)
                                status.postValue(
                                    Status.ErrorLoadingMore(
                                        errorResponse
                                    )
                                )
                            else
                                status.postValue(
                                    Status.Error(
                                        errorResponse
                                    )
                                )
                        } else if (isLoadMore)
                            setErrorLoadingMoreWithEmptyErrorResponse(R.string.some_thing_went_wrong_error_msg)
                        else
                            setErrorWithEmptyErrorResponse(R.string.some_thing_went_wrong_error_msg)
                    })
            )
            isLoadMore -> setErrorLoadingMoreWithEmptyErrorResponse(R.string.check_internet_connection)
            else -> setErrorWithEmptyErrorResponse(R.string.check_internet_connection)
        }
    }

    private fun clearSubscription() {
        if (!compositeDisposable.isDisposed) compositeDisposable.clear()
    }

    private fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun retry() {
        retry?.run()
    }

    private fun setRetry(action: Action?) {
        retry = action
    }

    // isForce = true invalidate the view if no network (case search)
    // isForce = false show only network error
    fun invalidate(isForce: Boolean = false) {
        if (isForce)
            doInvalidate()
        else if (!repository.isNetworkConnected())
            setErrorWithEmptyErrorResponse(R.string.check_internet_connection, true)
        else
            doInvalidate()
    }

    private fun doInvalidate() {
        status = SingleLiveEvent()
        clearSubscription()
        super.invalidate()
    }

    private fun setErrorWithEmptyErrorResponse(msg: Int, showOnlyErrorMsg: Boolean = false) {
        status.postValue(
            Status.Error(
                ErrorResponse<E>().also {
                    it.message =
                        repository.getString(msg)
                    it.showOnlyErrorMsg = showOnlyErrorMsg
                }
            )
        )
    }

    private fun setErrorLoadingMoreWithEmptyErrorResponse(msg: Int) {
        status.postValue(
            Status.ErrorLoadingMore(
                ErrorResponse<E>().also {
                    it.message =
                        repository.getString(msg)
                }
            )
        )
    }

    fun onCleared() {
        clearSubscription()
    }
}
