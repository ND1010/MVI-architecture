package com.example.mvisampleapp

import com.example.mvisampleapp.data.model.UserDto
import com.example.mvisampleapp.data.model.UserResponseDto
import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.remote.NetworkChecker
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.data.repository.UserRepositoryImpl
import com.example.mvisampleapp.domain.model.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var networkChecker: NetworkChecker

    @InjectMocks
    private lateinit var repository: UserRepositoryImpl

    @Test
    fun `getUser emits success when API call is successful`() = runTest {
        val dummyUsers = listOf(
            UserDto(1, "test@email.com", "John", "Doe", "avatar_url")
        )
        val mockResponseDto = UserResponseDto(data = dummyUsers)

        whenever(apiService.getUser()).thenReturn(mockResponseDto)
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)

        val results =  repository.getUser().toList()

        assert(results[0] is Result.Loading)

        val successResult = results[1] as Result.Success<List<User>>
        assertEquals(1, successResult.data.size)
        assertEquals("John Doe", successResult.data[0].name)
        assertEquals("test@email.com", successResult.data[0].email)
    }

    @Test
    fun `getUser emits Error then Api Throw Exception`() = runTest {
        whenever(apiService.getUser()).thenThrow(RuntimeException("Api error"))
        whenever(networkChecker.isNetworkAvailable()).thenReturn(true)

        val results =  repository.getUser().toList()

        assert(results[0] is Result.Loading)
        assert(results[1] is Result.Error)
        assertEquals("Api error", (results[1] as Result.Error).message)
    }

    @Test
    fun `getUser emits No Internet Error when network is unavailable`() = runTest {
        whenever(networkChecker.isNetworkAvailable()).thenReturn(false)

        val results = repository.getUser().toList()

        assert(results[0] is Result.Loading)

        val error = results[1] as Result.Error
        assertEquals("No internet connection", error.message)
        assertEquals(-1, error.code)
    }

}