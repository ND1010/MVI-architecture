package com.example.mvisampleapp.ui.userProfile

import com.example.mvisampleapp.domain.model.User

data class UserState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)
