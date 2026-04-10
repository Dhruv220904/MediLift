package com.projectasha.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projectasha.mobile.data.local.entities.VisitEntity

@Dao
interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: VisitEntity)

    @Query("SELECT * FROM visits WHERE synced = 0")
    suspend fun getUnsynced(): List<VisitEntity>

    @Query("UPDATE visits SET synced = 1, riskScore = :riskScore, riskLevel = :riskLevel, specialistFlag = :specialistFlag WHERE localId = :localId")
    suspend fun markSynced(localId: String, riskScore: Float?, riskLevel: String?, specialistFlag: String?)
}
