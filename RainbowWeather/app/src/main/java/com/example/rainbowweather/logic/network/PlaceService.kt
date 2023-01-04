package com.example.rainbowweather.logic.network

import com.example.rainbowweather.RainbowWeatherApplication
import com.example.rainbowweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    // Retrofit will send a GET request to access address in GET annotation
    @GET("v2/place?token=${RainbowWeatherApplication.TOKEN}&lang=zh_CN")
    // use Call<PlaceResponse> as return value to parse JSON to PlaceResponse
    fun searchPlaces(@Query("query") query:String): Call<PlaceResponse>
}