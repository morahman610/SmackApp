package com.example.smack.Controller

import android.app.Application
import com.example.smack.Utilities.SharedPreferences

class App: Application() {

    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }
    override fun onCreate() {
        sharedPreferences = SharedPreferences(applicationContext)
        super.onCreate()
    }
}