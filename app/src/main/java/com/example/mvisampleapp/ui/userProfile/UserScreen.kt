package com.example.mvisampleapp.ui.userProfile

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mvisampleapp.data.model.UserDto
import com.example.mvisampleapp.domain.model.User


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
                    // rememberLazyListState() = Helps Compose preserve scroll position and animations when list items change.
                    // Arrangement.spacedBy(8.dp) = Spacing / dividers between items in the list.
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 8.dp, end = 8.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        // key = { it.id } = Helps Compose preserve scroll position and animations when list items change.
                        items(state.users, key = { it.id }) { user ->
                            UserItem(user)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Card {
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
                    text = user.name,
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
                User(1, "abc@gmail.com", "Dhruv Nirmal", "image_url"),
                User(2, "xyz@gmail.com", "Bhoomi Chhatbar", "image_url"),
                User(3, "dhruv@gmail.com", "Chandni Ashara", "image_url"),
                User(4, "ndfhfh@gmail.com", "Tirth Ashara", "image_url"),
                User(4, "ndfhfh@gmail.com", "Mital Ashara", "image_url"),
            ),
            error = null
        )
    )
}

