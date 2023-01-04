package com.example.rainbowweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.rainbowweather.logic.Repository
import com.example.rainbowweather.logic.model.PlaceResponse.Place

class PlaceViewModel : ViewModel(){
    private val searchLiveData = MutableLiveData<String>()
    // to store city data in the interface
    // data about interface should be put in the ViewModel
    // to avoid data loss when screen translates
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    // where to use?
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavedPlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()

}