package com.starter.mvvm.ui.main.paging

import androidx.lifecycle.MutableLiveData
import com.starter.mvvm.data.local.entities.ListAndFirstUser
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.ui.main.MainRepository
import com.starter.mvvm.utils.Constants.FIRST_PAGE
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class MainDataSource(
    private val mainRepository: MainRepository,
    status: MutableLiveData<Status<BaseResponse<ListAndFirstUser>, Any>>,
) :
    BaseDataSource<User, BaseResponse<ListAndFirstUser>, Any>(mainRepository, status) {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, User>,
    ) {
        subscribe(
            returnZippedResponse(),
            { loadInitial(params, callback) },
            {
                callback.onResult(it?.data?.list ?: ArrayList(), null, FIRST_PAGE + 1)
            },
            false
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        subscribe(
            mainRepository.getUsers(params.key.toInt()).map { response ->
                BaseResponse<ListAndFirstUser>().also { newResponse ->
                    newResponse.data = ListAndFirstUser(response.data, null)
                }
            },
            { loadAfter(params, callback) },
            {
                callback.onResult(it?.data?.list ?: ArrayList(), params.key.toInt() + 1)
            },
            true
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        // pass
    }

    private fun returnZippedResponse(): Single<BaseResponse<ListAndFirstUser>> {
        return Single.zip(
            mainRepository.getUsers(FIRST_PAGE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()),
            mainRepository.getUser(1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()),
            BiFunction {
                    t1: BaseResponse<List<User>>,
                    t2: BaseResponse<User>,
                ->
                return@BiFunction BaseResponse<ListAndFirstUser>().also {
                    it.data =
                        ListAndFirstUser(
                            t1.data,
                            t2.data
                        )
                }
            }
        )
    }
}
