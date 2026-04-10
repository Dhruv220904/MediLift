package com.projectasha.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projectasha.mobile.data.local.entities.BeneficiaryEntity

@Dao
interface BeneficiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: BeneficiaryEntity)

    @Query("SELECT * FROM beneficiaries ORDER BY name ASC")
    suspend fun getAll(): List<BeneficiaryEntity>
}
