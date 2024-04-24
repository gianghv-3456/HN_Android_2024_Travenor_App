package com.example.travenor.core.network

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("name") val name: String, @SerializedName("id") val id: String)
data class UserList(@SerializedName("") val userList: List<User>)
