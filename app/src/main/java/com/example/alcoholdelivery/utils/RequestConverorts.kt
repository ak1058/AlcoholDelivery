package com.example.alcoholdelivery.utils

import androidx.room.TypeConverter
import com.example.alcoholdelivery.models.Volume
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class RequestConverters {

    @TypeConverter
    fun fromString(value: String?): Volume? {
        val type: Type = object : TypeToken<Volume?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromVolume(volume: Volume?): String? {
        val gson = Gson()
        return gson.toJson(volume)
    }







}