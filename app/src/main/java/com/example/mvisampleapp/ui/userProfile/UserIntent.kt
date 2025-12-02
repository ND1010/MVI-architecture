package com.example.mvisampleapp.ui.userProfile

sealed interface UserIntent {
    data object LoadUser : UserIntent
    data object Refresh : UserIntent
}