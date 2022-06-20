package com.bonustrack02.parktp

data class NaverUserInfoResponse(var resultcode : String, var message : String, var response : NaverIdUser)

data class NaverIdUser(var id : String, var email : String)
