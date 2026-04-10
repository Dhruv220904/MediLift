package com.projectasha.mobile.ui.visit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectasha.mobile.repository.AshaRepository
import kotlinx.coroutines.launch

class VisitViewModel(private val repository: AshaRepository) : ViewModel() {
    fun saveOfflineVisit(
        beneficiaryId: String,
        visitType: String,
        notes: String?,
        observations: List<Pair<String, String>>,
        onDone: (Result<String>) -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                repository.saveVisitOffline(beneficiaryId, visitType, notes, observations)
            }.onSuccess {
                onDone(Result.success(it))
            }.onFailure {
                onDone(Result.failure(it))
            }
        }
    }
}
