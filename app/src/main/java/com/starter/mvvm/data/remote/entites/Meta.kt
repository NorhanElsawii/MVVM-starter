package com.starter.mvvm.data.remote.entites

import com.google.gson.annotations.SerializedName

data class Meta(

    @field:SerializedName("path")
    val path: String? = null,

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("last_page")
    val lastPage: Int? = null,

    @field:SerializedName("from")
    val from: Any? = null,

    @field:SerializedName("to")
    val to: Any? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null,
)
