package com.projectasha.mobile.network.dto

data class ObservationDto(
    val key: String,
    val value: String,
)

data class VisitCreateRequest(
    val local_id: String,
    val beneficiary_id: String,
    val visit_type: String,
    val notes: String?,
    val observations: List<ObservationDto>,
)

data class VisitResponse(
    val id: String,
    val local_id: String?,
    val risk_score: Float?,
    val risk_level: String?,
    val specialist_flag: String?,
)
