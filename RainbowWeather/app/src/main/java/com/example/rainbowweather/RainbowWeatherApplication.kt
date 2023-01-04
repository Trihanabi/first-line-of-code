package com.example.rainbowweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class RainbowWeatherApplication: Application() {

    // Companion oeject has the same name of class
    companion object {
        @SuppressLint("StaticFieldLeak")
        // create context when it is needed
        lateinit var context: Context
        const val TOKEN = "y9FgthqwdBm7GqB8"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}