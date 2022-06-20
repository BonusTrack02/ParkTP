package com.bonustrack02.parktp

import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @Headers("Authorization: KakaoAK b96caaf968a0a761b94484d722357c7e")
    @GET("/v2/local/search/keyword.json")
    fun getJson(
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: String
    ): Call<ResponseItem>

    @GET("/v2/local/search/keyword.json")
    fun getScalarsJson(@Header("Authorization") auth : String,
                       @Query("query") query : String,
                       @Query("x") x : String,
                       @Query("y") y : String,
                       @Query("radius") radius : String) : Call<String>

    @Multipart
    @POST("/ParkTP/uploadReview.php")
    fun postReviewToServer(@PartMap dataPart : Map<String, String>) : Call<String>

    @GET("v1/nid/me")
    fun getNaverIdUserInfo(@Header("Authorization") authorization : String) : Call<NaverUserInfoResponse>
}