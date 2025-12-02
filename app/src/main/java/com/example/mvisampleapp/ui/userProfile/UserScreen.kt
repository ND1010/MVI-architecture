package com.example.mvisampleapp.ui.userProfile

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mvisampleapp.data.model.UserData


@Composable
fun UserScreen(viewModel: UserViewModel) {
    val state = viewModel.userState.collectAsState().value

    //First load
    LaunchedEffect(Unit) {
        viewModel.handleIntent(UserIntent.LoadUser)
    }

    UserScreenContent(
        state = state,
        onRetry = { viewModel.handleIntent(UserIntent.Refresh) }
    )
}


@Composable
fun UserScreenContent(
    state: UserState,
    onRetry: () -> Unit = {}
) {
    /// val state = viewModel.userState.collectAsState().value


    Scaffold { pedding ->
        Box(
            modifier = Modifier
                .padding(pedding)
                .fillMaxSize()
        ) {

            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.users) { user ->
                            UserItem(user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: UserData) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            AsyncImage(
                model = user.avatar,
                contentDescription = null,
                Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.email,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserScreenPreview_Success() {
    UserScreenContent(
        state = UserState(
            isLoading = false,
            users = listOf(
                UserData(1, "abc@gmail.com", "Dhruv", "Nirmal", ""),
                UserData(2, "xyz@gmail.com", "Bhoomi", "Chhatbar", ""),
                UserData(3, "dhruv@gmail.com", "Chandni", "Ashara", ""),
                UserData(4, "ndfhfh@gmail.com", "Tirth", "Ashara", ""),
                UserData(4, "ndfhfh@gmail.com", "Mital", "Ashara", ""),
            ),
            error = null
        )
    )
}

