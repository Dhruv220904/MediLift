package com.projectasha.mobile.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectasha.mobile.R
import com.projectasha.mobile.auth.TokenManager
import com.projectasha.mobile.databinding.ActivitySettingsBinding
import com.projectasha.mobile.ui.common.LocaleHelper

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    private val languageTags = listOf("en", "hi", "ta", "te", "or", "bn")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLanguageSpinner()

        binding.callSupportButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.call_help_support), Toast.LENGTH_SHORT).show()
        }

        binding.logoutButton.setOnClickListener {
            TokenManager(this).clear()
            Toast.makeText(this, getString(R.string.logout), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupLanguageSpinner() {
        val labels = listOf(
            getString(R.string.language_english),
            getString(R.string.language_hindi),
            getString(R.string.language_tamil),
            getString(R.string.language_telugu),
            getString(R.string.language_odia),
            getString(R.string.language_bengali),
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)
        binding.languageSpinner.adapter = adapter

        val currentTag = LocaleHelper.currentLanguageTag()
        val selectedIndex = languageTags.indexOfFirst { currentTag.startsWith(it) }.takeIf { it >= 0 } ?: 0
        binding.languageSpinner.setSelection(selectedIndex, false)

        binding.languageSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedTag = languageTags[position]
                val existing = LocaleHelper.currentLanguageTag()
                if (!existing.startsWith(selectedTag)) {
                    LocaleHelper.applyLanguage(selectedTag)
                    recreate()
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        })
    }
}
