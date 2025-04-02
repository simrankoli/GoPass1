package com.example.login_page.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BusPassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(busPass: BusPass)

    @Query("SELECT * FROM bus_pass WHERE studentId = :studentId")
    suspend fun getBusPassByStudentId(studentId: String): BusPass?

    @Query("SELECT * FROM bus_pass")
    suspend fun getAllBusPasses(): List<BusPass>

    @Query("UPDATE bus_pass SET expiryDate = :newExpiry WHERE studentId = :studentId")
    suspend fun updateExpiryDate(studentId: String, newExpiry: String)
}