package com.bonustrack02.parktp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper {

    companion object {
        fun getInstance(): Retrofit {
            val builder = Retrofit.Builder()
            builder.baseUrl("https://dapi.kakao.com")
            builder.addConverterFactory(GsonConverterFactory.create())

            return builder.build()
        }

        fun getScalarsInstance(): Retrofit {
            val builder = Retrofit.Builder()
            builder.baseUrl("http://bonustrack02.dothome.co.kr")
            builder.addConverterFactory(ScalarsConverterFactory.create())

            return builder.build()
        }

        fun getScalarsInstanceForTest() : Retrofit {
            val builder = Retrofit.Builder()
            builder.baseUrl("https://dapi.kakao.com")
            builder.addConverterFactory(ScalarsConverterFactory.create())

            return builder.build()
        }
    }
}