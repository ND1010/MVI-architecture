package com.example.mvisampleapp.ui.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

   // private val getUsersUseCase = GetUsersUseCase()

    private val _userState = MutableStateFlow(UserState())
    val userState : StateFlow<UserState> = _userState.asStateFlow()

    fun handleIntent(intent: UserIntent){
        viewModelScope.launch {
            when(intent){
                is UserIntent.LoadUser -> fetchUserProfile()
                is UserIntent.Refresh -> { fetchUserProfile() }
            }
        }
    }

    private suspend fun fetchUserProfile(){
        getUsersUseCase().collect { result ->
            when(result){
                is Result.Loading -> {
                    _userState.update { it.copy(isLoading = true, error = null) }
                }
                is Result.Success -> {
                    _userState.update { it.copy(isLoading = false, users = result.data.data) }
                }
                is  Result.Error -> {
                    _userState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

}