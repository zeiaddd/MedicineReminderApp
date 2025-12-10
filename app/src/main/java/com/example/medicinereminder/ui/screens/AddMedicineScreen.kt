package com.example.medicinereminder.ui.screens

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
// 🔑 Import the Experimental API class
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicinereminder.ui.viewmodel.AddMedicineState
import com.example.medicinereminder.ui.viewmodel.AddMedicineViewModel
import java.util.*

/**
 * Compose Screen for adding a new medicine and its dose schedule.
 * Uses AddMedicineViewModel to handle state and persistence.
 */
// 🔑 FIX: Add the definitive @OptIn annotation to acknowledge the use of TopAppBar/Scaffold
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    navController: NavController,
    // Hilt automatically provides the ViewModel instance
    viewModel: AddMedicineViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Collect UI state from the ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Collect input states from the ViewModel
    val name by viewModel.name.collectAsState()
    val dosage by viewModel.dosage.collectAsState()
    val selectedTimes by viewModel.selectedTimes.collectAsState()

    // Side Effect to handle navigation or error messages after saving
    LaunchedEffect(uiState) {
        when (uiState) {
            is AddMedicineState.Success -> {
                Toast.makeText(context, "Medicine saved and scheduled!", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // Go back to the Home Screen
            }
            is AddMedicineState.Error -> {
                val message = (uiState as AddMedicineState.Error).message
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Medicine") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Medicine Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.name.value = it },
                    label = { Text("Medicine Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 2. Dosage Input
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { viewModel.dosage.value = it },
                    label = { Text("Dosage (e.g., 5mg, 1 tablet)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                // 3. Dose Time Selection Section
                Text(
                    text = "Daily Dose Times:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Button to open Time Picker dialog
                TimePickerButton(
                    onTimeSelected = { newTime ->
                        // Add time only if it's unique
                        if (!selectedTimes.contains(newTime)) {
                            viewModel.selectedTimes.value = (selectedTimes + newTime).sorted()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 4. Display Selected Times (List)
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedTimes) { time ->
                        DoseTimeItem(
                            time = time,
                            onRemove = { timeToRemove ->
                                viewModel.selectedTimes.value = selectedTimes.filter { it != timeToRemove }
                            }
                        )
                    }
                }

                // 5. Save Button
                Button(
                    onClick = viewModel::saveMedicine,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = uiState != AddMedicineState.Loading
                ) {
                    if (uiState == AddMedicineState.Loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Save Medicine & Schedule")
                    }
                }
            }
        }
    )
}

// Helper Composable for selecting a time
@Composable
fun TimePickerButton(onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    // TimePickerDialog setup
    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                // Format time as HH:mm (e.g., 08:00)
                val timeString = String.format("%02d:%02d", selectedHour, selectedMinute)
                onTimeSelected(timeString)
            }, hour, minute, false // 24-hour format = true, 12-hour = false
        )
    }

    OutlinedButton(onClick = { timePickerDialog.show() }, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Default.Add, contentDescription = "Add Time")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Select Dose Time")
    }
}

// Helper Composable for displaying and removing a selected time
@Composable
fun DoseTimeItem(time: String, onRemove: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = { onRemove(time) }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remove Time",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}