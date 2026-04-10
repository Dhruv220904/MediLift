package com.projectasha.mobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.projectasha.mobile.data.local.dao.BeneficiaryDao
import com.projectasha.mobile.data.local.dao.VisitDao
import com.projectasha.mobile.data.local.dao.VisitObservationDao
import com.projectasha.mobile.data.local.entities.BeneficiaryEntity
import com.projectasha.mobile.data.local.entities.VisitEntity
import com.projectasha.mobile.data.local.entities.VisitObservationEntity

@Database(
    entities = [BeneficiaryEntity::class, VisitEntity::class, VisitObservationEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun beneficiaryDao(): BeneficiaryDao
    abstract fun visitDao(): VisitDao
    abstract fun visitObservationDao(): VisitObservationDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "asha_mobile.db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}
