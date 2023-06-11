package com.lorenzoprogramma.libraio.api

import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun loginUser(@Field("query") query: String): Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun registerUser(@Field("query") query: String): Call<JsonObject>

    @POST("postInsert/")
    fun findUser(@Body requestBody: RequestBody): Call<Boolean>


}