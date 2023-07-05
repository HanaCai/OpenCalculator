package com.google.androidcalculator.scriptcodeUI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.androidcalculator.roomdatabase.ScriptCodes
import com.google.androidcalculator.roomdatabase.ScriptCodesDatabase
import kotlinx.coroutines.launch

class AllCodesViewModel(param: AllCodesActivity) : ViewModel() {
    private val scriptDatabase by lazy { ScriptCodesDatabase.getDatabase(param).scriptCodeDao() }

    private var _allcodes = MutableLiveData<List<ScriptCodes>>()
    val allcodes: LiveData<List<ScriptCodes>> = _allcodes

    fun saveCode(code: Int, script: String) {
        val scriptCodes = ScriptCodes(
            code.toInt(),
            script
        )
        viewModelScope.launch {
            scriptDatabase.addScript(scriptCodes)
        }
    }

    fun getAllCode() {
        viewModelScope.launch {
            scriptDatabase.getScripts().collect { notesList ->
                if (notesList.isNotEmpty()) {
                    _allcodes.value = notesList
                }
            }
        }
    }
}