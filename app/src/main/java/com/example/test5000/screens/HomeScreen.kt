package com.example.test5000.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demo1.R
import com.example.test5000.viewModel.HomeViewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val cat = viewModel.catImage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Signoz Demo App",
            modifier = Modifier.padding(bottom = 32.dp),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5E35B1) // purple tone
        )

        // ---- Button #1: Start Trace ----
        Button(
            onClick = {
                Timber.tag("ButtonClicked").i("Start button clicked")
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5E35B1))
        ) {
            Text("Start Trace", fontSize = 16.sp, color = Color.White)
        }

        // ---- Button #2: Simulate Error ----
        Button(
            onClick = {
                try {
                    throw RuntimeException("Simulated crash for testing SDK")
                } catch (e: Exception) {
                    Timber.tag("ButtonClicked").e(e)
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)) // red
        ) {
            Text("Log Error", fontSize = 16.sp, color = Color.White)
        }

        // ---- Button #3: Fetch Cat Image ----
        Button(
            onClick = {
                viewModel.fetchCat()
                Timber.tag("ButtonClicked").i("Fetch Cat from API button clicked")
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(56.dp)
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)) // green
        ) {
            Text("Fetch Cat from API", fontSize = 16.sp, color = Color.White)
        }

        // Cat info display
        cat.value?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cat ID: ${it.id}", fontWeight = FontWeight.Medium)
            Text("URL: ${it.url}", fontWeight = FontWeight.Medium)
        }

        // ---- Button #4: Go To Log Screen ----
        OutlinedButton(
            onClick = {
                navController.navigate("logScreen")
                Timber.tag("ButtonClicked").i("Open Log Screen button clicked")
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF5E35B1))
        ) {
            Text("Open Log Screen", fontSize = 16.sp, color = Color(0xFF5E35B1))
        }
    }
}
