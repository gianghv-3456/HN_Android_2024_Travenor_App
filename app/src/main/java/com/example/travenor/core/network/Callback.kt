package com.example.travenor.core.network

import java.lang.reflect.Type

interface Call<T> {
    fun enqueue(parseType: Type, callback: Callback<T>)
}

interface Callback<T : Any?> {
    fun onResponse(rawResponse: String, response: Response<T>)
    fun onFailure(t: Throwable)
}

class Response<T>(val data: T)
