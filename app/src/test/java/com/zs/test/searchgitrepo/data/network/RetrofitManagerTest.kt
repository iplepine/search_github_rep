package com.zs.test.searchgitrepo.data.network

import com.zs.test.searchgitrepo.data.network.api.GitService
import com.zs.test.searchgitrepo.data.network.entity.Repository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.HttpException

class RetrofitManagerTest {

    private val gitService by lazy {
        RetrofitManager.createService(GitService::class.java)
    }

    @Test
    fun testSearchAndroid() {
        runBlocking {
            val query = "android"
            val result = gitService.getRepos(query, 0)
            assert(result.totalCount > 0)
            assert(result.items[0].name.contains(query))
        }
    }

    @Test
    fun testSearchInvalidPage() {
        runBlocking {
            try {
                val query = "Android"
                gitService.getRepos(query, 11)
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
            val result = gitService.getRepos(query, 0)
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
                gitService.getRepos(query, 0)
            } catch (e: HttpException) {
                val error = RetrofitManager.parseError(e.response())
                RetrofitManager.parseError(e.response())
                RetrofitManager.parseError(e.response())
                RetrofitManager.parseError(e.response())
                assert(error.getErrorMessage().contains("longer than 256"))
            }
        }
    }

    @Test
    fun testSearch5Pages() {
        runBlocking {
            val list = ArrayList<Repository>()
            val query = "android"
            var page = 0

            repeat(5) {
                val result = gitService.getRepos(query, page++)
                list.addAll(result.items)
            }

            assert(list.size == 500)
        }
    }

    @Test
    fun testSearchTime() {
        runBlocking {
            val start = System.currentTimeMillis()
            gitService.getRepos("android", 0)
            val end = System.currentTimeMillis()
            println("time : ${end-start}ms")
        }
    }
}