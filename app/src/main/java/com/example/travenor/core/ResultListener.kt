package com.example.travenor.core

interface ResultListener<T> {
    fun onSuccess(data: T?)
    fun onError(exception: Exception?)
}
