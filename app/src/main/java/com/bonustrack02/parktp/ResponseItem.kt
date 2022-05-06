package com.bonustrack02.parktp

data class ResponseItem(var meta : MetaClass, var documents : MutableList<Item>)

data class MetaClass(var pageable_count : Int, var total_count : Int, var is_end : Boolean)

data class Item(var place_name : String,
                var distance : String,
                var place_url : String,
                var category_name : String,
                var address_name : String,
                var road_address_name : String,
                var id : String,
                var phone : String,
                var category_group_name : String,
                var x : String,
                var y : String
                )