package com.zs.test.searchgitrepo.data.network.entity

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("resource")
    val resource: String? = null,

    @SerializedName("field")
    val field: String? = null,

    @SerializedName("code")
    val code: String? = null
)