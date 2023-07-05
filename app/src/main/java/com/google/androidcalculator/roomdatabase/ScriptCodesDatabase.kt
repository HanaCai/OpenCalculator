package com.google.androidcalculator.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ScriptCodes::class],
    version = 1,
    exportSchema = true
)
abstract class ScriptCodesDatabase : RoomDatabase() {

    abstract fun scriptCodeDao(): ScriptCodesDAO

    companion object {

        @Volatile
        private var INSTANCE: ScriptCodesDatabase? = null

        fun getDatabase(context: Context): ScriptCodesDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): ScriptCodesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ScriptCodesDatabase::class.java,
                "notes_database"
            )
                .build()
        }
    }
}