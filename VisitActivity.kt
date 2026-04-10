package com.projectasha.mobile.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectasha.mobile.AshaApp
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivityVisitBinding
import com.projectasha.mobile.ui.common.SessionStore
import kotlinx.coroutines.launch

class VisitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVisitBinding
    private lateinit var sessionStore: SessionStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionStore = SessionStore(this)

        sessionStore.getBeneficiaryId()?.let { binding.beneficiaryIdInput.setText(it) }
        binding.saveVisitButton.setOnClickListener { saveOfflineVisit() }
    }

    private fun saveOfflineVisit() {
        val beneficiaryId = binding.beneficiaryIdInput.text.toString().trim()
        val visitType = binding.visitTypeInput.text.toString().trim().ifBlank { "home" }
        val systolic = binding.systolicInput.text.toString().trim()
        val hemo = binding.hemoglobinInput.text.toString().trim()

        if (beneficiaryId.isBlank()) {
            Toast.makeText(this, getString(R.string.toast_enter_beneficiary_id), Toast.LENGTH_SHORT).show()
            return
        }

        val observations = mutableListOf<Pair<String, String>>()
        if (systolic.isNotBlank()) observations += "systolic_bp" to systolic
        if (hemo.isNotBlank()) observations += "hemoglobin" to hemo
        if (observations.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_observation), Toast.LENGTH_SHORT).show()
            return
        }

        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching {
                app.repository.saveVisitOffline(
                    beneficiaryId = beneficiaryId,
                    visitType = visitType,
                    notes = "Recorded from multi-page Android flow",
                    observations = observations,
                )
            }.onSuccess {
                binding.statusText.text = "${getString(R.string.status_visit_saved)}: $it"
                Toast.makeText(this@VisitActivity, getString(R.string.toast_visit_saved), Toast.LENGTH_SHORT).show()
            }.onFailure {
                binding.statusText.text = "${getString(R.string.status_visit_failed)}: ${it.message}"
                Toast.makeText(this@VisitActivity, getString(R.string.toast_visit_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
