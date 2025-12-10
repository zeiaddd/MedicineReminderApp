package com.example.medicinereminder


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel // Used to get Hilt ViewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medicinereminder.ui.screens.AddMedicineScreen // Will create soon
import com.example.medicinereminder.ui.screens.HomeScreen // Will create soon
import com.example.medicinereminder.ui.theme.MedicineReminderTheme // Use your app's theme
import dagger.hilt.android.AndroidEntryPoint // 🔑 HILT Import

/**
 * 🔑 Hilt Activity Entry Point: Marks the Activity as a Hilt entry point,
 * allowing Hilt to create and provide the scope for the ViewModels.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This function replaces the traditional setContentView(R.layout.activity_main)
        setContent {
            // Apply the custom theme for your Compose UI
            MedicineReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // This is the starting point of your entire Compose UI hierarchy
                    MedicineReminderAppNavigation()
                }
            }
        }
    }
}

// =========================================================================
// UI Navigation Setup (Lab 6)
// =========================================================================

/**
 * Defines the navigation structure for the application.
 */
@Composable
fun MedicineReminderAppNavigation() {
    // Controller manages the navigation stack (Lab 6)
    val navController = rememberNavController()

    // NavHost holds all your screens
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 1. Home Screen
        composable(Screen.Home.route) {
            // Hilt automatically finds and provides the HomeViewModel
            HomeScreen(
                navController = navController,
                // viewModel = hiltViewModel() // Example of getting the VM
            )
        }

        // 2. Add Medicine Screen
        composable(Screen.AddMedicine.route) {
            AddMedicineScreen(
                navController = navController,
                // viewModel = hiltViewModel()
            )
        }

        // TODO: Add History Screen composable later
    }
}

// Utility sealed class for defining screen routes
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object AddMedicine : Screen("add_medicine_screen")
    object History : Screen("history_screen")
    // If you add a Flutter Screen, this is where you'd define its Intent launch route
}