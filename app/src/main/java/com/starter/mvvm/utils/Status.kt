package com.starter.mvvm.utils

import com.starter.mvvm.data.remote.ErrorResponse

/**
 * Created by Norhan Elsawi on 5/10/2021.
 */
sealed class Status {

    data class Success<D>(
        var data: D?,
    ) : Status()

    object SuccessLoadingMore : Status()

    data class Error<E>(
        var errorResponse: ErrorResponse<E>,
    ) : Status()

    data class ErrorLoadingMore<E>(var errorResponse: ErrorResponse<E>) :
        Status()

    object Loading : Status()

    object LoadingMore : Status()
}
