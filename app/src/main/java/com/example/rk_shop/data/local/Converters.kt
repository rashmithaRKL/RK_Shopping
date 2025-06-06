package com.example.rk_shop.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromDoubleList(value: String?): List<Double>? {
        return value?.split(",")?.map { it.trim().toDouble() }
    }

    @TypeConverter
    fun doubleListToString(list: List<Double>?): String? {
        return list?.joinToString(",")
    }
}
