package com.lorenzoprogramma.libraio.data

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("username")
    var username: String?,
    @SerializedName("password")
    var userPassword: String?
)
