package com.example.travenor.core.network

import com.example.travenor.core.network.annotation.GET
import com.example.travenor.core.network.annotation.Path

interface ExampleApiDefine {
    @GET("/users/{id}")
    fun getUsername(@Path("id") id: String): Callback<String>
}
