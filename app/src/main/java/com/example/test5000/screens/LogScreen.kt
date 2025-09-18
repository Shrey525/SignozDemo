package com.example.test5000.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import io.opentelemetry.sdk.resources.Resource
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
            .padding(24.dp)
            .background(colorResource(id = R.color.white)),        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Log Event",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DropdownMenuBox(
                options = logLevels,
                selected = selectedLogLevel,
                onSelectedChange = { selectedLogLevel = it },
                modifier = Modifier.weight(0.5f)
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()
            )
            DropdownMenuBox(
                options = eventTypes,
                selected = selectedEvent,
                onSelectedChange = { selectedEvent = it },
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()
            )

            DropdownMenuBox(
                options = messages,
                selected = selectedMessage,
                onSelectedChange = { selectedMessage = it },
                modifier = Modifier.weight(0.7f)
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                Timber.tag("LogScreen").i("[$selectedLogLevel] $selectedEvent - $selectedMessage")
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
        ) {
            Text("Log Event")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
        ) {
            Text("Go Back")
        }
    }
}

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { expanded = true }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelectedChange(option)
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