package com.starter.mvvm.utils.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.starter.mvvm.R
import com.starter.mvvm.data.remote.ErrorResponse
import com.starter.mvvm.utils.SingleLiveEvent
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.extensions.getErrorResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Norhan Elsawi on 4/10/2021.
 */
abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {

    val showLoginDialog = SingleLiveEvent<Boolean>()
    val showNetworkError = SingleLiveEvent<Boolean>()
    private val compositeDisposable = CompositeDisposable()

    fun <D, E> subscribe(
        single: Single<D>,
        status: MutableLiveData<Status<D, E>>,
    ) {
        if (repository.isNetworkConnected())
            compositeDisposable.add(
                single
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        status.postValue(Status.Loading)
                    }
                    .subscribe({ response ->
                        status.postValue(Status.Success(response))
                    }, { error ->
                        val errorResponse = error.getErrorResponse<E>()
                        if (errorResponse != null) {
                            status.postValue(
                                Status.Error(
                                    errorResponse
                                )
                            )
                        } else
                            status.postValue(
                                Status.Error(
                                    ErrorResponse<E>().also {
                                        it.message =
                                            repository.getString(R.string.some_thing_went_wrong_error_msg)
                                    }
                                )
                            )
                    })
            )
        else
            status.postValue(
                Status.Error(
                    ErrorResponse<E>().also {
                        it.message =
                            repository.getString(R.string.check_internet_connection)
                    }
                )
            )
    }

    fun getCurrentLanguage() = repository.getCurrentLanguage()

    fun setLanguage(language: String) = repository.setLanguage(language)

    fun isNetworkConnected(): Boolean {
        val isNetworkConnected = repository.isNetworkConnected()
        if (!isNetworkConnected)
            this.showNetworkError.postValue(true)
        return isNetworkConnected
    }

//    fun isUserLogin(showLoginPopUp: Boolean = true): Boolean {
//        val isUserLogin = repository.isUserLogin()
//        if (!isUserLogin && showLoginPopUp)
//            this.showLoginDialog.postValue(true)
//        return isUserLogin
//    }

    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun clearSubscription() {
        if (compositeDisposable.isDisposed.not()) compositeDisposable.clear()
    }

    override fun onCleared() {
        clearSubscription()
        super.onCleared()
    }
}
