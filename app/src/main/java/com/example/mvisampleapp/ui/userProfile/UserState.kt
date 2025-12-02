package com.example.mvisampleapp.ui.userProfile

import com.example.mvisampleapp.data.model.UserData

data class UserState(
    val isLoading: Boolean = false,
    val users: List<UserData> = emptyList(),
    val error: String? = null
)
