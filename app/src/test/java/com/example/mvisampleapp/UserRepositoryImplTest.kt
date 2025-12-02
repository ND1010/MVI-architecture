package com.example.mvisampleapp

import app.cash.turbine.test
import com.example.mvisampleapp.data.model.UserData
import com.example.mvisampleapp.data.model.UserResponse
import com.example.mvisampleapp.data.remote.ApiService
import com.example.mvisampleapp.data.remote.NetworkClient
import com.example.mvisampleapp.data.remote.Result
import com.example.mvisampleapp.data.repository.UserRepositoryImpl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.whenever
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    @Mock
    lateinit var apiService: ApiService

    private lateinit var repository: UserRepositoryImpl
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        // Mock static NetworkClient.api
        mockStatic(NetworkClient::class.java).use { staticMock ->
            staticMock.`when`<Any> { NetworkClient.api }.thenReturn(apiService)
            repository = UserRepositoryImpl()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUser returns success`() = runTest {
        val dummyResponse = UserResponse(
            page = 1,
            perPage = 6,
            total = 12,
            totalPages = 2,
            data = listOf(
                UserData(1, "test@mail.com", "George", "Bluth", "avatar.jpg")
            )
        )

        whenever(apiService.getUser()).thenReturn(dummyResponse)

        repository.getUser().test {
            assert(awaitItem() is Result.Loading)

            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals(1, (success as Result.Success).data.data[0].id)

            cancelAndIgnoreRemainingEvents()
        }
    }

}