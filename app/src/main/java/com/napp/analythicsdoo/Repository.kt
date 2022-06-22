package com.napp.analythicsdoo

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlin.coroutines.*


class Repository(private val retrofit: Retrofit) {

    companion object {
        private const val BASE_URL = "https://api.github.com"

        fun build(): Repository {
            val client = OkHttpClient.Builder().apply{
                addInterceptor(tokenHeaderInterceptor(BuildConfig.GITLAB_TOKEN))
            }.build()

            val retrofit = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(client)
            }
            return Repository(retrofit.build())
        }
    }

    suspend fun <R, S> call(service: Class<S>, call: (service: S) -> Call<R>): R =
        suspendCoroutine { cont ->
            call.invoke(retrofit.create(service)).enqueue(cont.callBack())
        }
}

class NetworkException(val code: Int) : Exception()

interface IRepo {
    @GET("user/repos")
    fun getUserRepoList(): Call<List<Repo>>
}