package com.example.login_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.login_page.data.database.BusPassDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BusPassViewModel(application: Application) : AndroidViewModel(application) {
    private val db = BusPassDatabase.getDatabase(application).busPassDao()

    private val _passDetails = MutableStateFlow<BusPass?>(null)
    val passDetails = _passDetails.asStateFlow()

    val context = application.applicationContext

    init {
        fetchPassDetails("12345") // Replace with actual student ID
    }

    fun fetchPassDetails(studentId: String) {
        viewModelScope.launch {
            _passDetails.value = db.getBusPassByStudentId(studentId) as BusPass?
        }
    }

    suspend fun renewPass(duration: String): Boolean {
        val studentId = _passDetails.value?.studentId ?: return false
        val newExpiryDate = calculateNewExpiry(duration)
        return try {
            db.updateExpiryDate(studentId, newExpiryDate)
            fetchPassDetails(studentId) // Refresh details
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun calculateNewExpiry(duration: String): String {
        // Logic to calculate new expiry date based on duration
        return "2025-12-31" // Example fixed date, update as needed
    }
}