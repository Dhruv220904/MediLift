package com.projectasha.mobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectasha.mobile.databinding.ActivityAiRiskResultBinding

class AiRiskResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAiRiskResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiRiskResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.escalateButton.setOnClickListener {
            startActivity(Intent(this, EscalationActivity::class.java))
        }
    }
}
