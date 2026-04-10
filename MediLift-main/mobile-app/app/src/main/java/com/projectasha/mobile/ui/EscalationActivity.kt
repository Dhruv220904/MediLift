package com.projectasha.mobile.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivityEscalationBinding

class EscalationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEscalationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEscalationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.callDoctorButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.call_doctor), Toast.LENGTH_SHORT).show()
        }

        binding.confirmReferralButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.confirm_referral), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
