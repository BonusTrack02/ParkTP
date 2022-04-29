package com.bonustrack02.parktp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("/v2/local/search/keyword.json")
    fun getParkJson(@Header("Authorization") auth : String,
                    @Query("query") query : String,
                    @Query("x") x : String,
                    @Query("y") y : String,
                    @Query("radius") radius : String
    ) : Call<ResponseItem>

    @GET("/v2/local/search/keyword.json")
    fun getScalarsJson(@Header("Authorization") auth : String,
                       @Query("query") query : String,
                       @Query("x") x : String,
                       @Query("y") y : String,
                       @Query("radius") radius : String) : Call<String>
}