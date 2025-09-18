package com.example.test5000

import android.app.Application
import com.example.test5000.util.OpenTelemetryTree
import com.example.test5000.util.OpenTelemetryUtil
import okhttp3.internal.platform.android.AndroidLog
import timber.log.Timber

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start OpenTelemetry
        OpenTelemetryUtil.init()

        // Plant Timber tree
        Timber.plant(OpenTelemetryTree())

        if (OpenTelemetryUtil.logger == null) {
            android.util.Log.e("OTelTree", "Logger is null, skipping export")
            return
        }

        Timber.i("Test from Timber → should go through OTelTree")
    }
}