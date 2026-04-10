package com.projectasha.mobile.repository

import com.projectasha.mobile.auth.TokenManager
import com.projectasha.mobile.data.local.AppDatabase
import com.projectasha.mobile.data.local.entities.BeneficiaryEntity
import com.projectasha.mobile.data.local.entities.VisitEntity
import com.projectasha.mobile.data.local.entities.VisitObservationEntity
import com.projectasha.mobile.network.ApiService
import com.projectasha.mobile.network.dto.BeneficiaryCreateRequest
import com.projectasha.mobile.network.dto.LoginRequest
import com.projectasha.mobile.network.dto.ObservationDto
import com.projectasha.mobile.network.dto.RefreshRequest
import com.projectasha.mobile.network.dto.RegisterRequest
import com.projectasha.mobile.network.dto.SyncRequest
import com.projectasha.mobile.network.dto.SyncVisit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class AshaRepository(
    private val api: ApiService,
    private val db: AppDatabase,
    private val tokenManager: TokenManager,
) {
    suspend fun registerAsha(
        name: String,
        email: String,
        password: String,
        district: String?,
    ) = withContext(Dispatchers.IO) {
        api.register(
            RegisterRequest(
                name = name,
                email = email,
                password = password,
                role = "asha",
                district = district,
            ),
        )
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        val token = api.login(LoginRequest(email = email, password = password))
        tokenManager.saveTokens(token.access_token, token.refresh_token)
        token
    }

    suspend fun refreshTokenIfAvailable() = withContext(Dispatchers.IO) {
        val refresh = tokenManager.getRefreshToken() ?: return@withContext null
        val token = api.refresh(RefreshRequest(refresh_token = refresh))
        tokenManager.saveTokens(token.access_token, token.refresh_token)
        token
    }

    suspend fun createBeneficiaryOfflineFirst(
        name: String,
        gender: String?,
        dob: String?,
        pregnancyStatus: Boolean,
        village: String?,
    ) = withContext(Dispatchers.IO) {
        val remote = api.createBeneficiary(
            BeneficiaryCreateRequest(name, gender, dob, pregnancyStatus, village),
        )
        db.beneficiaryDao().upsert(
            BeneficiaryEntity(
                id = remote.id,
                name = remote.name,
                gender = remote.gender,
                dob = remote.dob,
                pregnancyStatus = remote.pregnancy_status,
                village = remote.village,
            ),
        )
        remote
    }

    suspend fun saveVisitOffline(
        beneficiaryId: String,
        visitType: String,
        notes: String?,
        observations: List<Pair<String, String>>,
    ): String = withContext(Dispatchers.IO) {
        val localId = UUID.randomUUID().toString()
        db.visitDao().upsert(
            VisitEntity(
                localId = localId,
                beneficiaryId = beneficiaryId,
                visitType = visitType,
                notes = notes,
                synced = false,
            ),
        )
        db.visitObservationDao().insertAll(
            observations.map { (key, value) ->
                VisitObservationEntity(visitLocalId = localId, key = key, value = value)
            },
        )
        localId
    }

    suspend fun syncPendingVisits() = withContext(Dispatchers.IO) {
        val pendingVisits = db.visitDao().getUnsynced()
        if (pendingVisits.isEmpty()) return@withContext 0

        val payload = pendingVisits.map { visit ->
            val obs = db.visitObservationDao().byVisitLocalId(visit.localId).map {
                ObservationDto(key = it.key, value = it.value)
            }
            SyncVisit(
                local_id = visit.localId,
                beneficiary_id = visit.beneficiaryId,
                visit_type = visit.visitType,
                notes = visit.notes,
                observations = obs,
            )
        }

        val result = api.syncBatch(SyncRequest(visits = payload))
        result.results.forEach { item ->
            if (item.status == "synced" || item.status == "already_synced") {
                db.visitDao().markSynced(
                    localId = item.local_id,
                    riskScore = item.risk_score,
                    riskLevel = item.risk_level,
                    specialistFlag = item.specialist_flag,
                )
            }
        }
        result.synced
    }

    suspend fun pendingLocalCount(): Int = withContext(Dispatchers.IO) {
        db.visitDao().getUnsynced().size
    }
}
