package com.bonustrack02.parktp

import android.app.Application
import android.content.res.Resources
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val nativeAppKey = resources.getString(R.string.kakao_native_key)
        KakaoSdk.init(this, nativeAppKey)
    }
}