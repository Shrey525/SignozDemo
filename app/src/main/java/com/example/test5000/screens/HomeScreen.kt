package com.example.test5000.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demo1.R
import com.example.test5000.viewModel.HomeViewModel
import timber.log.Timber

@Composable
fun HomeScreen(navController: NavController,
               viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val cat = viewModel.catImage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Signoz Demo App",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.height(50.dp))
        // ---- Button #1: Start Trace ----
        Button(
            onClick = {
                // Log info through TelemetryLogger wrapper
                Timber.tag("ButtonClicked").i("Start button clicked")

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
                    Timber.tag("ButtonClicked").e(e)
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text("Log Error")
        }

        // ---- Button #3: Fetch Cat Image from API Call ----
        Button(
            onClick = {
                viewModel.fetchCat()
                Timber.tag("ButtonClicked").i("Fetch Cat from API button clicked")
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text("Fetch Cat from API")
        }

        cat.value?.let {
            Text("Cat ID: ${it.id}")
            Text("URL: ${it.url}")
        }

        // ---- Button #4: Go To Log Screen ----
        Button(
            onClick = {
                navController.navigate("logScreen")
                Timber.tag("ButtonClicked").i("Open Log Screen button clicked")
            },
            modifier =  Modifier
                .padding(8.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text("Open Log Screen")
        }
    }
}