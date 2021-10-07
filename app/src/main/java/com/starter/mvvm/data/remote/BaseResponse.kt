package com.starter.mvvm.data.remote

import com.google.gson.annotations.SerializedName
import com.starter.mvvm.data.remote.entites.Meta

open class BaseResponse<D> {

    @field:SerializedName("message")
    val message: String = ""

    @field:SerializedName("data")
    var data: D? = null

    @field:SerializedName("meta")
    var meta: Meta? = null
}
