package com.example.medicinereminder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicinereminder.Screen // Import the Screen utility class
import com.example.medicinereminder.ui.viewmodel.HomeViewModel

/**
 * The main screen that displays the list of scheduled medicines.
 */
@Composable
// Since Scaffold and TopAppBar are often experimental, we apply the OptIn here too.
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        // 1. Top Bar for the screen title
        topBar = {
            TopAppBar(title = { Text("My Medicine Reminders") })
        },

        // 2. 🔑 FIX: Floating Action Button for navigation
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigate to the Add Medicine Screen
                    navController.navigate(Screen.AddMedicine.route)
                },
                // Use the standard plus icon
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Add Medicine")
                }
            )
        },

        // 3. Main content area
        content = { paddingValues ->
            // Placeholder for the list of medicines (Lab 7/8)
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // This is where you will eventually put the LazyColumn for medicine list
                Text(
                    text = "No medicines scheduled. Tap '+' to add a reminder.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}