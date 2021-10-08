package com.starter.mvvm.ui.details

import com.starter.mvvm.data.local.entities.TwoUsers
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.utils.SingleLiveEvent
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val detailsRepository: DetailsRepository) :
    BaseViewModel(detailsRepository) {

    val status = SingleLiveEvent<Status<BaseResponse<TwoUsers>, Any>>()

    fun getTwoUsers(id: Int) {
        subscribe(returnZippedResponse(id), status)
    }

    private fun returnZippedResponse(id: Int): Single<BaseResponse<TwoUsers>> {
        return Single.zip(
            detailsRepository.getUser(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()),
            detailsRepository.getUser(id + 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()),
            BiFunction {
                    t1: BaseResponse<User>,
                    t2: BaseResponse<User>,
                ->
                return@BiFunction BaseResponse<TwoUsers>().also {
                    it.data =
                        TwoUsers(
                            t1.data,
                            t2.data
                        )
                }
            }
        )
    }
}
