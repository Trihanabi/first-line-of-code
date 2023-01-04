package com.example.rainbowweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.rainbowweather.logic.Repository
import com.example.rainbowweather.logic.model.PlaceResponse

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<PlaceResponse.Location>()
    // data related to interface
    var locationLng = ""
    var locationLat = ""
    var placeName = ""
    // to observe object
    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = PlaceResponse.Location(lng, lat)
    }
}
