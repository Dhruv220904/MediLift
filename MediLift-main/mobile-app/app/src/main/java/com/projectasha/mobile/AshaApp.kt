package com.projectasha.mobile

import android.app.Application
import com.projectasha.mobile.auth.TokenManager
import com.projectasha.mobile.data.local.AppDatabase
import com.projectasha.mobile.network.ApiClient
import com.projectasha.mobile.repository.AshaRepository

class AshaApp : Application() {
    lateinit var repository: AshaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val tokenManager = TokenManager(this)
        val db = AppDatabase.getInstance(this)
        val api = ApiClient.create(tokenManager)
        repository = AshaRepository(api, db, tokenManager)
    }
}
