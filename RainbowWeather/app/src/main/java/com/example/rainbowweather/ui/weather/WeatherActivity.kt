package com.example.rainbowweather.ui.weather

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rainbowweather.R
import com.example.rainbowweather.databinding.*
import com.example.rainbowweather.logic.model.Weather
import com.example.rainbowweather.logic.model.getSky
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    var currentTemp: TextView ?= null
    var currentSky: TextView ?= null
    var currentAQI: TextView ?= null
    var nowLayout: RelativeLayout ?= null
    var forecastLayout: LinearLayout ?= null
    var coldRiskText: TextView ?= null
    var placeName: TextView ?= null

    var dressingText: TextView ?= null
    var ultravioletText: TextView ?= null
    var carWashingText: TextView ?= null
    var weatherLayout: ScrollView ? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        Log.d("WeatherActivity", "0.0")
        setContentView(R.layout.activity_weather)
//        weatherL = ActivityWeatherBinding.inflate(layoutInflater)
//        life = LifeIndexBinding.inflate(layoutInflater)
        // to get lng and lat
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        // to observe weatherLiveData
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                Log.d("WeatherActivity", "before showWeaterInfo()")
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        currentTemp = findViewById(R.id.currentTemp)
        currentSky = findViewById(R.id.currentSky)
        currentAQI = findViewById(R.id.currentSky)
        nowLayout = findViewById(R.id.nowLayout)
        forecastLayout = findViewById(R.id.forecastLayout)
        coldRiskText = findViewById(R.id.coldRiskText)
        placeName = findViewById(R.id.placeName)
        dressingText = findViewById(R.id.dressingText)
        ultravioletText = findViewById(R.id.ultravioletText)
        carWashingText = findViewById(R.id.carWashingText)
        weatherLayout = findViewById(R.id.weatherLayout)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showWeatherInfo(weather: Weather) {
        Log.d("WeatherActivity", "In showWeaterInfo()")
        placeName?.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // display data in now.xml
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp?.text = currentTempText
        currentSky?.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI?.text = currentPM25Text
        nowLayout?.setBackgroundResource(getSky(realtime.skycon).bg)
        // display data in forecast.xml
        forecastLayout?.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout?.addView(view)
        }
        // display data in life_index.xml
        val lifeIndex = daily.lifeIndex
        coldRiskText?.text = lifeIndex.coldRisk[0].desc
        dressingText?.text = lifeIndex.dressing[0].desc
        ultravioletText?.text = lifeIndex.ultraviolet[0].desc
        carWashingText?.text = lifeIndex.carWashing[0].desc
        weatherLayout?.visibility = View.VISIBLE
    }

}