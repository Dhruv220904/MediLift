package com.projectasha.mobile.network.dto

data class BeneficiaryCreateRequest(
    val name: String,
    val gender: String?,
    val dob: String?,
    val pregnancy_status: Boolean,
    val village: String?,
)

data class BeneficiaryResponse(
    val id: String,
    val asha_id: String,
    val name: String,
    val gender: String?,
    val dob: String?,
    val pregnancy_status: Boolean,
    val village: String?,
)
