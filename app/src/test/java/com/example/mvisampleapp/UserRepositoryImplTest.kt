package com.example.mvisampleapp

import com.example.mvisampleapp.data.model.UserData
import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.data.repository.UserRepositoryImpl
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

    @InjectMocks
    private lateinit var repository: UserRepositoryImpl

    @Test
    fun `getUser emits success when API call is successful`() = runTest {
        val dummyUser = listOf(UserData(1, "test@email.com", "John", "Doe", "avatar_url"))
        val mockResponse = UserResponse(data =dummyUser )

        whenever(apiService.getUser()).thenReturn(mockResponse)

        val results =  repository.getUser().toList()

        assert(results[0] is Result.Loading)
        assert(results[1] is Result.Success)
        assertEquals("John", (results[1] as Result.Success).data.data[0].firstName)

    }

    @Test
    fun `getUser emits Error then Api Throw Exception`() = runTest {
        whenever(apiService.getUser()).thenThrow(RuntimeException("Network error"))

        val results =  repository.getUser().toList()

        assert(results[0] is Result.Loading)
        assert(results[1] is Result.Error)
        assertEquals("Network error", (results[1] as Result.Error).message)
    }

}