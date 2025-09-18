package com.example.test5000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.test5000.util.OpenTelemetryUtil
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test5000.screens.HomeScreen
import com.example.test5000.screens.LogScreen
import timber.log.Timber


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This sets up tracer + logger providers and registers them globally
        Timber.i("MainActivity: Activity started")
        OpenTelemetryUtil.logger
            ?.logRecordBuilder()
            ?.setSeverity(io.opentelemetry.api.logs.Severity.INFO)
            ?.setBody("Hello from OTel Logs")
            ?.emit()
        Timber.i("Hello from Timber")
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { HomeScreen(navController) }
                composable("logScreen") { LogScreen(navController) }
            }
        }
    }
}