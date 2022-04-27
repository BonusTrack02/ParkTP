package com.bonustrack02.parktp

data class ResponseItem(var meta : MetaClass, var documents : MutableList<Item>)

data class MetaClass(var pageableCount : Int, var totalCount : Int, var isEnd : Boolean)

data class Item(var placeName : String,
                var distance : String,
                var placeUrl : String,
                var categoryName : String,
                var addressName : String,
                var roadAddressName : String,
                var phone : String,
                var categoryGroupName : String,
                var x : String,
                var y : String
                )