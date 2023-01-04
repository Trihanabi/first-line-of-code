package com.example.rainbowweather.logic.network

import com.example.rainbowweather.RainbowWeatherApplication
import com.example.rainbowweather.logic.model.DailyResponse
import com.example.rainbowweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// use in network

interface WeatherService {
    @GET("v2.5/${RainbowWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<RealtimeResponse>
    @GET("v2.5/${RainbowWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<DailyResponse>
}