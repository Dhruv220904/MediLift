package com.projectasha.mobile.network.dto

data class StatsResponse(
    val total_visits_today: Int,
    val high_risk_today: Int,
    val total_beneficiaries: Int,
    val active_ashas: Int,
)
