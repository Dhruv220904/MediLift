package com.projectasha.mobile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPatientFileButton.setOnClickListener {
            startActivity(Intent(this, PatientHistoryActivity::class.java))
        }

        binding.logMedicationButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.log_medication), Toast.LENGTH_SHORT).show()
        }
    }
}
