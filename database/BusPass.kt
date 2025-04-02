package com.example.login_page.data.database

import android.content.Context
import androidx.room.*

@Entity(tableName = "bus_pass")
data class BusPass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val studentId: String,
    val contact: String,
    val email: String,
    val course: String,
    val pickupLocation: String,
    val dropLocation: String,
    val paymentMode: String,
    val academicYear: String,
    val passDuration: String,
    val imageUri: String?,
    val expiryDate: String // Store image URI as String
)