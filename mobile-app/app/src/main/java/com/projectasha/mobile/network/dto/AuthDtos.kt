package com.projectasha.mobile.network.dto

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val district: String?,
)

data class RegisterResponse(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val district: String?,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RefreshRequest(
    val refresh_token: String,
)

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val user_id: String,
    val role: String,
    val name: String,
)
