package com.projectasha.mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beneficiaries")
data class BeneficiaryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val gender: String?,
    val dob: String?,
    val pregnancyStatus: Boolean,
    val village: String?,
)
