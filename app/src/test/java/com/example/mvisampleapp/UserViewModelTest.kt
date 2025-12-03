package com.example.mvisampleapp

import app.cash.turbine.test
import com.example.mvisampleapp.data.model.UserData
import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.domain.usecase.GetUsersUseCase
import com.example.mvisampleapp.ui.userProfile.UserIntent
import com.example.mvisampleapp.ui.userProfile.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(   MockitoJUnitRunner::class)
class UserViewModelTest {

    @InjectMocks
    lateinit var userViewModel: UserViewModel

    @Mock
    lateinit var getUsersUseCase: GetUsersUseCase

    @Test
    fun `handleIntent LoadUser updates state with success`() = runTest{
        val mockUserData = listOf(UserData(1, "test@gamil.com", "John", "Doe", "avatar_url"))
        val mockUserResponse = UserResponse(data = mockUserData)

        whenever(getUsersUseCase()).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Success(mockUserResponse))
            }
        )

        userViewModel.userState.test {
            userViewModel.handleIntent(UserIntent.LoadUser)

            val initialState = awaitItem()
            assert(!initialState.isLoading)
            assert(initialState.users.isEmpty())
            assert(initialState.error == null)

            // Second emission: loading state
            val loadingState = awaitItem()
            assert(loadingState.isLoading)
            assert(loadingState.users.isEmpty())
            assert(loadingState.error == null)

            // skipItems(2)

            // Third emission: success state
            val successState = awaitItem()
            assert(!successState.isLoading)
            assert(successState.users.size == 1)
            assert(successState.users[0].firstName == "John")
            assert(successState.error == null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent Refresh updates state with success`() = runTest{
        val mockUserData = listOf(UserData(1, "test@gamil.com", "John", "Doe", "avatar_url"))
        val mockUserResponse = UserResponse(data = mockUserData)

        whenever(getUsersUseCase()).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Success(mockUserResponse))
            }
        )

        userViewModel.userState.test {
            userViewModel.handleIntent(UserIntent.Refresh)

            skipItems(2)

            val successState = awaitItem()
            assert(!successState.isLoading)
            assert(successState.users.size == 1)
            assert(successState.users[0].firstName == "John")
            assert(successState.error == null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `handleIntent LoadUser updates states with error`() = runTest{
        whenever(getUsersUseCase()).thenReturn(
            flow {
                emit(Result.Loading)
                emit(Result.Error(500, "Something went wrong"))
            }
        )

        userViewModel.userState.test {
            userViewModel.handleIntent(UserIntent.LoadUser)

            skipItems(1)

            val loadingState = awaitItem()
            assert(loadingState.isLoading)

            val errorState = awaitItem()
            assert(!errorState.isLoading)
            assert(errorState.users.isEmpty())
            assert(errorState.error == "Something went wrong")

            cancelAndIgnoreRemainingEvents()

        }
    }

}