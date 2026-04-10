package com.projectasha.mobile.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.projectasha.mobile.AshaApp

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val app = applicationContext as AshaApp
        return try {
            app.repository.syncPendingVisits()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
