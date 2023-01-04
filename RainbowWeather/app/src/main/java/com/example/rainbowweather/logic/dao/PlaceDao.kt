package com.example.rainbowweather.logic.dao

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import com.example.rainbowweather.RainbowWeatherApplication
import com.example.rainbowweather.logic.model.PlaceResponse.Place
import com.google.gson.Gson

object PlaceDao {

    // to save Place object to SharedPreferences file
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }
    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }
    fun isPlaceSaved() = sharedPreferences().contains("place")
    private fun sharedPreferences() = RainbowWeatherApplication.context.
    getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
}