package com.example.musicappjgis.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicappjgis.data.Album
import com.example.musicappjgis.data.AlbumRepository
import com.example.musicappjgis.ui.UiState
import com.example.musicappjgis.ui.components.AlbumBigCard
import com.example.musicappjgis.ui.components.AlbumSmallCard
import com.example.musicappjgis.ui.components.MiniPlayer
import com.example.musicappjgis.ui.theme.Background
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme
import com.example.musicappjgis.ui.theme.PurpleLight
import com.example.musicappjgis.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onAlbumClick: (String) -> Unit) {
    val state = remember { mutableStateOf(UiState<List<Album>>(loading = true)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val result = AlbumRepository.getAlbums()
        state.value = result.fold(
            onSuccess = { UiState(loading = false, data = it) },
            onFailure = { UiState(loading = false, error = it.message ?: "Error") }
        )
    }

    fun retry() {
        state.value = UiState(loading = true)
        scope.launch {
            val result = AlbumRepository.getAlbums()
            state.value = result.fold(
                onSuccess = { UiState(loading = false, data = it) },
                onFailure = { UiState(loading = false, error = it.message ?: "Error") }
            )
        }
    }

    Scaffold(
        bottomBar = { MiniPlayer() }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PurpleLight, Background),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .padding(inner)
        ) {
            when {
                state.value.loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.value.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Algo salió mal")
                        Button(onClick = { retry() }) {
                            Text(text = "Retry")
                        }
                    }
                }
                else -> {
                    val albums = state.value.data ?: emptyList()
                    Column(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                            Text(text = "Good Morning!", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                            Text(text = "Gael Ibarra", style = MaterialTheme.typography.headlineLarge)
                        }
                        Text(text = "Albums", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(albums) { album ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(200.dp)
                                ) {
                                    AlbumBigCard(
                                        title = album.title,
                                        artist = album.artist,
                                        imageUrl = album.image,
                                        onClick = { onAlbumClick(album.id) },
                                        onPlayClick = {}
                                    )
                                }
                            }
                        }
                        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Recently Played", style = MaterialTheme.typography.titleLarge)
                        }
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(albums) { album ->
                                AlbumSmallCard(
                                    title = album.title,
                                    artist = album.artist,
                                    imageUrl = album.image,
                                    onClick = { onAlbumClick(album.id) }
                                )
                            }
                        }
                        Box(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MusicAppJGISTheme {
        HomeScreen(onAlbumClick = {})
    }
}
