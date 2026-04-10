package com.projectasha.mobile.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectasha.mobile.AshaApp
import com.projectasha.mobile.R
import com.projectasha.mobile.databinding.ActivitySyncStatusBinding
import kotlinx.coroutines.launch

class SyncStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySyncStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySyncStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        refreshPendingCount()

        binding.retrySyncButton.setOnClickListener {
            val app = application as AshaApp
            lifecycleScope.launch {
                runCatching { app.repository.syncPendingVisits() }
                    .onSuccess {
                        binding.statusText.text = "${getString(R.string.status_sync_complete)}: $it"
                        Toast.makeText(this@SyncStatusActivity, getString(R.string.toast_sync_done), Toast.LENGTH_SHORT).show()
                        refreshPendingCount()
                    }
                    .onFailure {
                        binding.statusText.text = "${getString(R.string.status_sync_failed)}: ${it.message}"
                        Toast.makeText(this@SyncStatusActivity, getString(R.string.toast_sync_failed), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun refreshPendingCount() {
        val app = application as AshaApp
        lifecycleScope.launch {
            runCatching { app.repository.pendingLocalCount() }
                .onSuccess {
                    binding.offlineRecordsText.text = "${getString(R.string.pending_sync)}: $it"
                }
                .onFailure {
                    binding.offlineRecordsText.text = getString(R.string.pending_sync_unknown)
                }
        }
    }
}
