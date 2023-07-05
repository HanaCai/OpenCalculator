package com.google.androidcalculator.roomdatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptCodesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addScript(note: ScriptCodes)

    @Query("SELECT * FROM scriptcodes ORDER BY code DESC")
    fun getScripts(): Flow<List<ScriptCodes>>

    @Update
    fun updateScript(note: ScriptCodes)

    @Delete
    fun deleteScript(note: ScriptCodes)
}