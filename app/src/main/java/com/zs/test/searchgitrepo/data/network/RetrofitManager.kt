package com.zs.test.searchgitrepo.data.network

import com.google.gson.Gson
import com.zs.test.searchgitrepo.data.network.entity.SearchResultError
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitManager {
    private const val BASE_URL = "https://api.github.com"

    private val gson: Gson by lazy {
        Gson()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            //Log.d("HttpLog :", it)
            println(it)
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <S> createService(serviceClass: Class<S>?): S {
        return retrofit.create(serviceClass)
    }

    fun parseError(response: Response<*>?): SearchResultError {
        val converter = retrofit.responseBodyConverter<SearchResultError>(
            SearchResultError::class.java,
            arrayOfNulls<Annotation>(0)
        )
        return try {
            converter.convert(response?.errorBody()) ?: SearchResultError()
        } catch (e: IOException) {
            return SearchResultError()
        }
    }
}