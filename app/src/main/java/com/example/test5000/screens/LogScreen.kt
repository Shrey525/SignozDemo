package com.example.test5000.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.demo1.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import timber.log.Timber

@Composable
fun LogScreen(navController: NavController) {

    val logLevels = listOf("Info", "Debug", "Error")
    val eventTypes = listOf("Button Clicked", "API Called", "Screen Loaded", "User Action")
    val messages = listOf("Success", "Failed", "Started")

    var selectedLogLevel by remember { mutableStateOf(logLevels[0]) }
    var selectedEvent by remember { mutableStateOf(eventTypes[0]) }
    var selectedMessage by remember { mutableStateOf(messages[0]) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(colorResource(id = R.color.white)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This is the Log Screen",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.black)
        )


        Spacer(modifier = Modifier.height(50.dp))

        // Row with 3 dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DropdownMenuBox(
                options = logLevels,
                selected = selectedLogLevel,
                onSelected = { selectedLogLevel = it }
            )

            DropdownMenuBox(
                options = eventTypes,
                selected = selectedEvent,
                onSelected = { selectedEvent = it }
            )

            DropdownMenuBox(
                options = messages,
                selected = selectedMessage,
                onSelected = { selectedMessage = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button row
        Button(onClick = {
            val logText = "$selectedEvent - $selectedMessage"
            when (selectedLogLevel) {
                "Info" -> Timber.i(logText)
                "Debug" -> Timber.d(logText)
                "Error" -> Timber.e(logText)
            }
        }) {
            Text("Log Event")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = MaterialTheme.shapes.extraSmall, // 👈 makes it more square
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selected)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown arrow",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun LogScreenPreview(){
    LogScreen(navController = rememberNavController())
}