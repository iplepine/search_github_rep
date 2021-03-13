package com.zs.test.searchgitrepo.data.network.entity

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val isInCompleteResults: Boolean,

    @SerializedName("items")
    val items: List<Repository>
)