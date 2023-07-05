package com.google.androidcalculator.scriptcodeUI

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.androidcalculator.MainActivity
import com.google.androidcalculator.PrefsManager
import com.google.androidcalculator.databinding.ActivityScriptDialogsBinding
import com.google.androidcalculator.roomdatabase.ScriptCodesDatabase

class ScriptDialogsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScriptDialogsBinding
    private lateinit var myPrefsManager: PrefsManager
    private val scriptDatabase by lazy { ScriptCodesDatabase.getDatabase(this).scriptCodeDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScriptDialogsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPrefsManager = PrefsManager(this)
        if (myPrefsManager.read() != 0) {
           naviagetToMain()
        }
        binding.btnNext.setOnClickListener { saveMasteKey() }

    }

    private fun naviagetToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun saveMasteKey() {
        if (binding.etMastercode.text!!.isNotEmpty()) {
            myPrefsManager.save(binding.etMastercode.text.toString().toInt())
            naviagetToMain()
        } else
            Toast.makeText(this, "Please enter master code", Toast.LENGTH_SHORT).show()
    }

}