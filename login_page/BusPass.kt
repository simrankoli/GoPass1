package com.example.login_page

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BusPass(
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
    val expiryDate: String
) : Parcelable