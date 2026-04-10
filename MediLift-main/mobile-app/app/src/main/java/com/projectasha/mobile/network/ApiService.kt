package com.projectasha.mobile.network

import com.projectasha.mobile.network.dto.BeneficiaryCreateRequest
import com.projectasha.mobile.network.dto.BeneficiaryResponse
import com.projectasha.mobile.network.dto.LoginRequest
import com.projectasha.mobile.network.dto.RefreshRequest
import com.projectasha.mobile.network.dto.RegisterRequest
import com.projectasha.mobile.network.dto.RegisterResponse
import com.projectasha.mobile.network.dto.SyncRequest
import com.projectasha.mobile.network.dto.SyncResponse
import com.projectasha.mobile.network.dto.TokenResponse
import com.projectasha.mobile.network.dto.VisitCreateRequest
import com.projectasha.mobile.network.dto.VisitResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): TokenResponse

    @POST("beneficiary/")
    suspend fun createBeneficiary(@Body request: BeneficiaryCreateRequest): BeneficiaryResponse

    @POST("visit/")
    suspend fun createVisit(@Body request: VisitCreateRequest): VisitResponse

    @POST("sync/batch")
    suspend fun syncBatch(@Body request: SyncRequest): SyncResponse

    @GET("stats/")
    suspend fun getStats(): com.projectasha.mobile.network.dto.StatsResponse
}
