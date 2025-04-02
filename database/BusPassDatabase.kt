package com.example.login_page.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BusPass::class], version = 1, exportSchema = false)
abstract class BusPassDatabase : RoomDatabase() {
    abstract fun busPassDao(): BusPassDao

    companion object {
        @Volatile
        private var INSTANCE: BusPassDatabase? = null

        fun getDatabase(context: Context): BusPassDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusPassDatabase::class.java,
                    "bus_pass_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}