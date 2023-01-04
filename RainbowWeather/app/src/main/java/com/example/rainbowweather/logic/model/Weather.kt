package com.example.rainbowweather.logic.model


// to encapsulate Realtime and Daily object
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily) {
}