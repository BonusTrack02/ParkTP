package com.bonustrack02.parktp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class RetrofitHelper {

    companion object {
        fun getInstance(): Retrofit {
            val builder = Retrofit.Builder()
            builder.baseUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
            builder.addConverterFactory(GsonConverterFactory.create())
            val retrofit = builder.build()

            return retrofit
        }
    }
}