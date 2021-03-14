package com.zs.test.searchgitrepo.data.network.api

import com.zs.test.searchgitrepo.data.network.entity.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface GitService {
    @GET("/search/repositories")
    suspend fun getRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): SearchResult
}