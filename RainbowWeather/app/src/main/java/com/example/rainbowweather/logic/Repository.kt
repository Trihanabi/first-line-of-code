package com.example.rainbowweather.logic

import androidx.lifecycle.liveData
import com.example.rainbowweather.logic.dao.PlaceDao
import com.example.rainbowweather.logic.model.PlaceResponse.Place
import com.example.rainbowweather.logic.model.Weather
import com.example.rainbowweather.logic.network.RainbowWeatherNetwork
import kotlinx.coroutines.*


object Repository {

    // set LiveData to dispatchers to let all the code in sub-thread
    // because android doesn't allow network request in the main thread
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        // to use network func to get placeResponse
        val placeResponse = RainbowWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    // to use lng nad lat to get weather
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                RainbowWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                RainbowWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime,
                    dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineDispatcher, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()



}