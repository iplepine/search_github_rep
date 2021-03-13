package com.zs.test.searchgitrepo.data.network

import com.zs.test.searchgitrepo.data.network.api.SearchReposService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException

class RetrofitManagerTest {

    private val api by lazy {
        RetrofitManager.createService(SearchReposService::class.java)
    }

    @Test
    fun testSearchAndroid() {
        runBlocking {
            val query = "android"
            val result = api.getRepos(query, 0)
            assert(result.totalCount > 0)
            assert(result.items[0].name.contains(query))
        }
    }

    @Test
    fun testSearchInvalidPage() {
        runBlocking {
            try {
                val query = "Android"
                val result = api.getRepos(query, 11)
            } catch (e: HttpException) {
                val error = RetrofitManager.parseError(e.response())
                assert(error.getErrorMessage().contains("1000 search results"))
            }
        }
    }

    @Test
    fun testComplexQuery() {
        runBlocking {
            val query = "liverecyclerview user:iplepine"
            val result = api.getRepos(query, 0)
            assert(result.totalCount == 1)
        }
    }

    @Test
    fun testOver256Query() {
        runBlocking {
            val query = let {
                val builder = StringBuilder()
                for (i in 0 until 260) {
                    builder.append(i)
                }
                builder.toString()
            }
            try {
                val result = api.getRepos(query, 0)
            } catch (e: HttpException) {
                val error = RetrofitManager.parseError(e.response())
                assert(error.getErrorMessage().contains("longer than 256"))
            }
        }
    }
}