package com.projectasha.mobile.ui.common

import android.content.Context

class SessionStore(context: Context) {
    private val prefs = context.getSharedPreferences("asha_ui_session", Context.MODE_PRIVATE)

    fun saveBeneficiaryId(id: String) {
        prefs.edit().putString(KEY_BENEFICIARY_ID, id).apply()
    }

    fun getBeneficiaryId(): String? = prefs.getString(KEY_BENEFICIARY_ID, null)

    companion object {
        private const val KEY_BENEFICIARY_ID = "beneficiary_id"
    }
}
