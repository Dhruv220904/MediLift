package com.projectasha.mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visit_observations")
data class VisitObservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val visitLocalId: String,
    val key: String,
    val value: String,
)
