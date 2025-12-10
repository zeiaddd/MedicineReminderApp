package com.example.medicinereminder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicinereminder.data.db.entities.Medicine
import com.example.medicinereminder.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel // 🔑 HILT IMPORT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject // 🔑 HILT IMPORT

// Sealed class to represent UI state (Good practice for Compose)
sealed class AddMedicineState {
    object Idle : AddMedicineState()
    object Loading : AddMedicineState()
    object Success : AddMedicineState()
    data class Error(val message: String) : AddMedicineState()
}

@HiltViewModel // 1. Tells Hilt this is a ViewModel
class AddMedicineViewModel @Inject constructor( // 2. Hilt injects the Repository
    private val repository: MedicineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddMedicineState>(AddMedicineState.Idle)
    val uiState: StateFlow<AddMedicineState> = _uiState

    // State for inputs
    val name = MutableStateFlow("")
    val dosage = MutableStateFlow("")
    val selectedTimes = MutableStateFlow(listOf<String>()) // e.g., ["08:00", "14:00"]

    fun saveMedicine() {
        // ... (Input validation logic goes here) ...

        _uiState.value = AddMedicineState.Loading

        viewModelScope.launch {
            try {
                // Simplified date calculation
                val now = System.currentTimeMillis()
                val oneMonthLater = now + (30L * 24 * 60 * 60 * 1000)

                val newMedicine = Medicine(
                    name = name.value,
                    dosage = dosage.value,
                    startDate = now,
                    endDate = oneMonthLater
                )

                // Calls the Repository, which handles saving to Room and scheduling with WorkManager
                repository.saveMedicineAndSchedule(newMedicine, selectedTimes.value)
                _uiState.value = AddMedicineState.Success

                // Reset inputs
                name.value = ""
                dosage.value = ""
                selectedTimes.value = emptyList()

            } catch (e: Exception) {
                _uiState.value = AddMedicineState.Error("Save failed: ${e.localizedMessage}")
            }
        }
    }
}