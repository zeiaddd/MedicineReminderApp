package com.example.medicinereminder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicinereminder.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Placeholder data class for combined UI presentation
data class TodayDoseDisplay(
    // ... define fields needed for Home Screen cards ...
    val name: String,
    val time: String,
    val isPending: Boolean
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {

    // Example of exposing data from the repository as StateFlow for Compose
    val allMedicines = repository.getAllMedicines().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // TODO: Implement the complex logic to combine all data into today's schedule
    val todayDoses: StateFlow<List<TodayDoseDisplay>> = allMedicines
        .map { medicines ->
            // In a real project, this is where you'd call ScheduleCalculator to
            // merge schedules and history for the day.
            listOf(TodayDoseDisplay("Sample Pill", "08:00", true))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}