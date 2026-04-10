package com.projectasha.mobile.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectasha.mobile.AshaApp
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivityBeneficiaryBinding
import com.projectasha.mobile.ui.common.SessionStore
import kotlinx.coroutines.launch

class BeneficiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBeneficiaryBinding
    private lateinit var sessionStore: SessionStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeneficiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionStore = SessionStore(this)

        binding.createBeneficiaryButton.setOnClickListener { createBeneficiary() }
    }

    private fun createBeneficiary() {
        val name = binding.nameInput.text.toString().trim()
        val village = binding.villageInput.text.toString().trim().ifBlank { null }
        if (name.isBlank()) {
            Toast.makeText(this, getString(R.string.toast_enter_name), Toast.LENGTH_SHORT).show()
            return
        }

        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching {
                app.repository.createBeneficiaryOfflineFirst(
                    name = name,
                    gender = "F",
                    dob = "1998-01-01",
                    pregnancyStatus = true,
                    village = village,
                )
            }.onSuccess {
                sessionStore.saveBeneficiaryId(it.id)
                binding.beneficiaryIdText.text = "${getString(R.string.beneficiary_id)}: ${it.id}"
                binding.statusText.text = "${getString(R.string.status_beneficiary_created)}: ${it.id}"
                Toast.makeText(this@BeneficiaryActivity, getString(R.string.toast_beneficiary_created), Toast.LENGTH_SHORT).show()
            }.onFailure {
                binding.statusText.text = "${getString(R.string.status_beneficiary_failed)}: ${it.message}"
                Toast.makeText(this@BeneficiaryActivity, getString(R.string.toast_beneficiary_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
