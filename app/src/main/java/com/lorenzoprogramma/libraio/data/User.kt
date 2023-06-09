package com.lorenzoprogramma.libraio.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var userID: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("surname")
    var surname: String?,
    @SerializedName("password")
    var userPassword: String?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("conduct")
    var conduct: Int?

)
