package com.zs.test.searchgitrepo.data.network.entity

import com.google.gson.annotations.SerializedName

data class SearchResultError(
    @SerializedName("message")
    val message: String = "Error",

    @SerializedName("errors")
    val errors: List<ApiError> = emptyList(),

    @SerializedName("documentation_url")
    val documentationUrl: String? = null
) {

    fun getErrorMessage(): String {
        return message + errors.getOrNull(0)?.toString()
    }
}