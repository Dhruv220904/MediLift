package com.projectasha.mobile.ui.beneficiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectasha.mobile.repository.AshaRepository
import kotlinx.coroutines.launch

class BeneficiaryViewModel(private val repository: AshaRepository) : ViewModel() {
    fun createBeneficiary(
        name: String,
        gender: String?,
        dob: String?,
        pregnancyStatus: Boolean,
        village: String?,
        onDone: (Result<String>) -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                repository.createBeneficiaryOfflineFirst(name, gender, dob, pregnancyStatus, village).id
            }.onSuccess {
                onDone(Result.success(it))
            }.onFailure {
                onDone(Result.failure(it))
            }
        }
    }
}
