package com.lorenzoprogramma.libraio.api

import com.google.gson.JsonObject
import com.lorenzoprogramma.libraio.R
import com.lorenzoprogramma.libraio.data.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun loginUser(@Field("query") query: String): Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun registerUser(@Field ("query") query: String): Call<JsonObject>

    @POST("postSelect/")
    @FormUrlEncoded
    fun findUser(@Field("query") query: String): Call<JsonObject>

    @POST("postSelect/")
    @FormUrlEncoded
    fun getUserInfo(@Field("query") query: String): Call<JsonObject>

    @POST("postSelect/")
    @FormUrlEncoded
    fun getBook(@Field("query") query: String): Call<JsonObject>
}