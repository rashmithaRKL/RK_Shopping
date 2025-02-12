package com.example.rk_shop.data.local

import androidx.room.TypeConverter
import com.example.rk_shop.data.model.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromReviewList(value: String?): List<Review> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toReviewList(list: List<Review>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringMap(value: String?): Map<String, String> {
        if (value == null) return emptyMap()
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toStringMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    @TypeConverter
    fun toBigDecimal(bigDecimal: BigDecimal?): String? {
        return bigDecimal?.toString()
    }
}
