package com.example.test5000

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test5000.util.OpenTelementryLogger
import com.example.test5000.util.OpenTelemetryUtil
import io.opentelemetry.context.Context


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize OpenTelemetry before anything else
        // This sets up tracer + logger providers and registers them globally
        OpenTelemetryUtil.init()
        OpenTelementryLogger.i("MainActivity", "Activity started")

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Load the UI
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---- Button #1: Start Trace ----
        Button(
            onClick = {
                // Log info through TelemetryLogger wrapper
                OpenTelementryLogger.i("MainActivity", "Start button clicked")

                // Start parent → child trace chain
                parentSpan()
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text("Start Trace")
        }

        // ---- Button #2: Simulate Error ----
        Button(
            onClick = {
                try {
                    // Simulated crash for testing error tracking
                    throw RuntimeException("Simulated crash for testing SDK")
                } catch (e: Exception) {
                    // Send error log to SigNoz via OpenTelemetry
                    OpenTelementryLogger.e("MainActivity", "Error logged", e)

                    // Also start a trace to show span relationship
                    parentSpan()
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text("Log Error")
        }
    }
}

// ---- Tracing Example ----
fun parentSpan() {
    // Get tracer from utility
    val tracer = OpenTelemetryUtil.getTracer()

    // Start a parent span
    val span = tracer?.spanBuilder("Parent Span")?.startSpan()
    try {
        // Set current context → allows child span linking
        span?.makeCurrent().use {
            println("Parent traceId: ${span?.spanContext?.traceId}")
            println("Parent spanId: ${span?.spanContext?.spanId}")

            // Create child span under this parent
            childSpan()
        }
    } finally {
        // Always end span
        span?.end()
    }
}

fun childSpan() {
    val tracer = OpenTelemetryUtil.getTracer()

    // Start child span, inheriting parent context
    val span = tracer?.spanBuilder("Child Span")
        ?.setParent(Context.current())
        ?.startSpan()
    try {
        span?.makeCurrent().use {
            println("Child traceId: ${span?.spanContext?.traceId}")
            println("Child spanId: ${span?.spanContext?.spanId}")
        }
    } finally {
        span?.end()
    }
}

