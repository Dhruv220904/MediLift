package com.projectasha.mobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectasha.mobile.databinding.ActivityPatientHistoryBinding

class PatientHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
