package com.projectasha.mobile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectasha.mobile.AshaApp
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivityMainBinding
import com.projectasha.mobile.sync.SyncScheduler
import com.projectasha.mobile.ui.common.LocaleHelper
import com.projectasha.mobile.ui.common.SessionStore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionStore: SessionStore

    private val languageTags = listOf("en", "hi", "ta", "te", "or", "bn")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionStore = SessionStore(this)
        SyncScheduler.schedulePeriodic(this)

        setupLanguageSpinner()

        binding.addPatientButton.setOnClickListener { startActivity(Intent(this, BeneficiaryActivity::class.java)) }
        binding.startVisitButton.setOnClickListener { startActivity(Intent(this, VisitActivity::class.java)) }
        binding.aiRiskButton.setOnClickListener { startActivity(Intent(this, AiRiskResultActivity::class.java)) }
        binding.escalationButton.setOnClickListener { startActivity(Intent(this, EscalationActivity::class.java)) }
        binding.notificationsButton.setOnClickListener { startActivity(Intent(this, NotificationsActivity::class.java)) }
        binding.patientHistoryButton.setOnClickListener { startActivity(Intent(this, PatientHistoryActivity::class.java)) }
        binding.syncStatusButton.setOnClickListener { startActivity(Intent(this, SyncStatusActivity::class.java)) }
        binding.settingsButton.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }

        binding.syncNowButton.setOnClickListener {
            SyncScheduler.runNow(this)
            syncNow()
        }

        binding.statusText.text = getString(R.string.status_ready)
    }

    override fun onResume() {
        super.onResume()
        refreshDashboardBits()
    }

    private fun refreshDashboardBits() {
        val id = sessionStore.getBeneficiaryId() ?: "-"
        binding.beneficiaryHintText.text = "${getString(R.string.beneficiary_id)}: $id"

        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching { app.repository.pendingLocalCount() }
                .onSuccess {
                    binding.pendingSyncText.text = "${getString(R.string.pending_sync)}: $it"
                }
                .onFailure {
                    binding.pendingSyncText.text = getString(R.string.pending_sync_unknown)
                }
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

    private fun syncNow() {
        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching { app.repository.syncPendingVisits() }
                .onSuccess {
                    binding.statusText.text = "${getString(R.string.status_sync_complete)}: $it"
                    Toast.makeText(this@MainActivity, getString(R.string.toast_sync_done), Toast.LENGTH_SHORT).show()
                    refreshDashboardBits()
                }
                .onFailure {
                    binding.statusText.text = "${getString(R.string.status_sync_failed)}: ${it.message}"
                    Toast.makeText(this@MainActivity, getString(R.string.toast_sync_failed), Toast.LENGTH_SHORT).show()
                }
        }
    }
}
