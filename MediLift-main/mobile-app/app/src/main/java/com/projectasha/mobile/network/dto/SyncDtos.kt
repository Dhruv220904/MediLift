package com.projectasha.mobile.network.dto

data class SyncVisit(
    val local_id: String,
    val beneficiary_id: String,
    val visit_type: String,
    val notes: String?,
    val observations: List<ObservationDto>,
)

data class SyncRequest(
    val visits: List<SyncVisit>,
)

data class SyncResult(
    val local_id: String,
    val status: String,
    val risk_score: Float?,
    val risk_level: String?,
    val specialist_flag: String?,
)

data class SyncResponse(
    val synced: Int,
    val results: List<SyncResult>,
)
