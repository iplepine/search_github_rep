package com.zs.test.searchgitrepo.data.network.entity

import com.google.gson.annotations.SerializedName

data class Repository(
    @SerializedName("full_name")
    val name: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("stargazer_count")
    val starCount: Int = 0,

    @SerializedName("updated_at")
    val update: String = "",

    @SerializedName("url")
    val url: String? = null
)