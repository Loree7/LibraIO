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
import retrofit2.http.Url

interface UserAPI {

    @POST("postSelect/")
    @FormUrlEncoded
    fun select(@Field("query") query: String): Call<JsonObject>

    @POST("postInsert/")
    @FormUrlEncoded
    fun insert(@Field ("query") query: String): Call<JsonObject>

    @GET
    fun get(@Url url: String) : Call <ResponseBody>

    @POST("postRemove/")
    @FormUrlEncoded
    fun delete(@Field ("query") query: String): Call<JsonObject>

    @POST("postUpdate/")
    @FormUrlEncoded
    fun update(@Field ("query") query: String): Call<JsonObject>
}