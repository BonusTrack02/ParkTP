package com.bonustrack02.parktp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

open interface RetrofitService {
    @GET("/v2/local/search/keyword.json?{query}")
    fun getParkJson(@Header("Authorization") auth : String,
                    @Query("query") query : String,
                    @Query("x") x : String,
                    @Query("y") y : String,
                    @Query("radius") radius : String
    ) : Call<ResponseItem>
}