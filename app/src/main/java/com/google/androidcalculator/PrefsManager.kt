package com.google.androidcalculator

import android.content.Context

class PrefsManager(context: Context) {
    private val file = "com.darlempire78.opencalculator"
    private val key = "master_key"
    private var sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun save(masterCode: Int) {
        editor.putInt(key, masterCode)
        editor.apply()
    }

    fun read(): Int {
        return sharedPreferences.getInt(key, 0)
    }
}