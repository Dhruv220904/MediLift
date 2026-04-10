package com.projectasha.mobile.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectasha.mobile.AshaApp
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailInput.setText("asha@example.com")
        binding.passwordInput.setText("pass123")

        binding.registerButton.setOnClickListener { registerAsha() }
        binding.loginButton.setOnClickListener { login() }
    }

    private fun registerAsha() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, getString(R.string.toast_enter_email_password), Toast.LENGTH_SHORT).show()
            return
        }

        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching {
                app.repository.registerAsha(
                    name = "ASHA Worker",
                    email = email,
                    password = password,
                    district = "Patna",
                )
            }.onSuccess {
                binding.statusText.text = "${getString(R.string.status_registered)}: ${it.email}"
                Toast.makeText(this@LoginActivity, getString(R.string.toast_registered), Toast.LENGTH_SHORT).show()
            }.onFailure {
                binding.statusText.text = "${getString(R.string.status_registration_note)}: ${it.message}"
                Toast.makeText(this@LoginActivity, getString(R.string.toast_register_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login() {
        val email = binding.emailInput.text.toString().trim()
        val password = binding.passwordInput.text.toString()
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, getString(R.string.toast_enter_email_password), Toast.LENGTH_SHORT).show()
            return
        }

        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching { app.repository.login(email, password) }
                .onSuccess {
                    binding.statusText.text = "${getString(R.string.status_login_success)}: ${it.name} (${it.role})"
                    Toast.makeText(this@LoginActivity, getString(R.string.toast_logged_in), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .onFailure {
                    binding.statusText.text = "${getString(R.string.status_login_failed)}: ${it.message}"
                    Toast.makeText(this@LoginActivity, getString(R.string.toast_login_failed), Toast.LENGTH_SHORT).show()
                }
        }
    }
}
