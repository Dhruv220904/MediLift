package com.projectasha.mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visits")
data class VisitEntity(
    @PrimaryKey val localId: String,
    val beneficiaryId: String,
    val visitType: String,
    val notes: String?,
    val synced: Boolean,
    val riskScore: Float? = null,
    val riskLevel: String? = null,
    val specialistFlag: String? = null,
)
