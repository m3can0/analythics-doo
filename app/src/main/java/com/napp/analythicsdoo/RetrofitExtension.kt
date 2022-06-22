package com.napp.analythicsdoo

import okhttp3.Interceptor
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal fun <T> Continuation<T>.callBack() = object : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            resume(response.body())
            return
        }

        resumeWithException(NetworkException(response.code()))
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        resumeWithException(t)
    }
}

internal fun tokenHeaderInterceptor(token :String) = Interceptor { chain ->
    val request: Request = chain.request().let { original ->
        original.newBuilder().apply {
            header("Authorization", "token $token")
            method(original.method(), original.body())
        }.build()
    }
    chain.proceed(request)
}