package com.example.musicappjgis.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicappjgis.data.Album
import com.example.musicappjgis.data.AlbumRepository
import com.example.musicappjgis.ui.UiState
import com.example.musicappjgis.ui.components.AlbumBigCard
import com.example.musicappjgis.ui.components.AlbumSmallCard
import com.example.musicappjgis.ui.components.MiniPlayer
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme
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

    val mini = state.value.data?.firstOrNull()
    val miniImage = mini?.image ?: ""
    val miniTitle = mini?.title ?: "Tales of Ithiria"
    val miniArtist = mini?.artist ?: "Haggard"

    Scaffold(
        bottomBar = { MiniPlayer(imageUrl = miniImage, title = miniTitle, artist = miniArtist) }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .background(Brush.verticalGradient(listOf(Color(0xFFF3ECFF), Color(0xFFF8F5FF))))
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
                        Button(onClick = { retry() }) { Text(text = "Retry") }
                    }
                }
                else -> {
                    val albums = state.value.data ?: emptyList()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 96.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                                    .background(
                                        brush = Brush.verticalGradient(listOf(Color(0xFF7A52FF), Color(0xFF9E7CFF))),
                                        shape = MaterialTheme.shapes.extraLarge
                                    )
                                    .padding(horizontal = 20.dp, vertical = 24.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "Good Morning!", color = Color(0xFFEFE7FF), style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Gael Ibarra", color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Albums", style = MaterialTheme.typography.titleLarge)
                                Text(text = "See more", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF7A52FF))
                            }
                        }
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(albums) { album ->
                                    Box(modifier = Modifier.width(220.dp)) {
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
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Recently Played", style = MaterialTheme.typography.titleLarge)
                                Text(text = "See more", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF7A52FF))
                            }
                        }
                        items(albums) { album ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                AlbumSmallCard(
                                    title = album.title,
                                    artist = album.artist,
                                    imageUrl = album.image,
                                    onClick = { onAlbumClick(album.id) }
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.height(4.dp)) }
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
