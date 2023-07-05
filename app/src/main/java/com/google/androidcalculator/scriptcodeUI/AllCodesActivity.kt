package com.google.androidcalculator.scriptcodeUI

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.androidcalculator.PrefsManager
import com.google.androidcalculator.R
import com.google.androidcalculator.databinding.ActivityAllCodesBinding
import com.google.androidcalculator.roomdatabase.ScriptCodes
import com.google.androidcalculator.roomdatabase.ScriptCodesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class AllCodesActivity : AppCompatActivity() {
    private val TAG = "AllCodesActivity"
    private val scriptDatabase by lazy { ScriptCodesDatabase.getDatabase(this).scriptCodeDao() }
    private val rolePlayAdapter = AllCodesAdapter()
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAllCodesBinding.inflate(layoutInflater)
    }
    private val viewModel: AllCodesViewModel by lazy {
        ViewModelProvider(this, factory).get(AllCodesViewModel::class.java)
    }
    private var dialog: Dialog? = null
    private lateinit var myPrefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        myPrefsManager = PrefsManager(this)
        intializeDialog()

        binding.rvCodes.apply {
            adapter = rolePlayAdapter
        }
        binding.imageViewAddcode.setOnClickListener {
            showDialog(null)
        }
        getAllList()
        rolePlayAdapter.itemClickListener { scriptCodes, view ->
            showDialog(scriptCodes)
        }
    }

    private fun intializeDialog() {
        dialog = Dialog(this, R.style.Theme_Dialog)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_custom_layout)
    }

    private fun getAllList() {
        lifecycleScope.launch {
            scriptDatabase.getScripts().collect { notesList ->
                if (notesList.isNotEmpty()) {
                    rolePlayAdapter.setContentList(notesList as MutableList<ScriptCodes>)
                }
            }
        }
    }

    private fun showDialog(scriptCodes: ScriptCodes?) {
        val buttonSave = dialog!!.findViewById(R.id.btn_save) as AppCompatButton
        val buttonviewAll = dialog!!.findViewById(R.id.btn_viewall) as AppCompatButton
        buttonviewAll.visibility = View.GONE
        val editTextScriptCode = dialog!!.findViewById(R.id.et_scriptcode) as AppCompatEditText
        val editTextScript = dialog!!.findViewById(R.id.et_script) as AppCompatEditText
        editTextScriptCode.isEnabled = true
        editTextScript.text!!.clear()
        editTextScriptCode.text!!.clear()
        scriptCodes?.let {
            editTextScriptCode.setText(scriptCodes.code.toString())
            editTextScript.setText(scriptCodes.script)
            editTextScriptCode.isEnabled = false
        }
        buttonSave.setOnClickListener {
            if (editTextScript.text!!.isNotEmpty() && editTextScriptCode.text!!.isNotEmpty()) {
                if (scriptCodes != null) {
                    updateScriptCode(
                        editTextScript.text.toString().trim(),
                        editTextScriptCode.text.toString().trim()
                    )
                    dialog!!.dismiss()
                } else {
                    if (editTextScriptCode.text.toString() != myPrefsManager.read().toString()) {
                        saveScriptandCode(
                            editTextScript.text.toString().trim(),
                            editTextScriptCode.text.toString().trim()
                        )
                        dialog!!.dismiss()
                    } else {
                        Toast.makeText(
                            this,
                            "Please change you code this is master code",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


        }
        dialog!!.show()
    }

    private fun updateScriptCode(script: String, code: String) {
        val scriptCodes = ScriptCodes(
            code.toInt(),
            script
        )
        lifecycleScope.async(Dispatchers.IO) {
            scriptDatabase?.updateScript(scriptCodes)
        }
        rolePlayAdapter.notifyDataSetChanged()

    }

    private fun saveScriptandCode(script: String, code: String) {
        val scriptCodes = ScriptCodes(
            code.toInt(),
            script
        )
        addScript(scriptCodes)
        Toast.makeText(this, "Script added successfully", Toast.LENGTH_SHORT).show()

    }

    private fun addScript(scriptCodes: ScriptCodes) {
        lifecycleScope.launch(Dispatchers.IO) {
            scriptDatabase.addScript(scriptCodes)
        }
        getAllList()
    }

    var factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllCodesViewModel(this@AllCodesActivity) as T
        }
    }
}