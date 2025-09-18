package com.example.test5000

import android.app.Application
import com.example.test5000.util.OpenTelemetryUtil

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        OpenTelemetryUtil.init()
    }
}