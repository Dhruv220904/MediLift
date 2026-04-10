package com.projectasha.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projectasha.mobile.data.local.entities.VisitObservationEntity

@Dao
interface VisitObservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<VisitObservationEntity>)

    @Query("SELECT * FROM visit_observations WHERE visitLocalId = :localId")
    suspend fun byVisitLocalId(localId: String): List<VisitObservationEntity>
}
