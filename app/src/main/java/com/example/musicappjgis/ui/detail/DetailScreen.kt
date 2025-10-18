package com.example.musicappjgis.ui.detail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicappjgis.data.Album
import com.example.musicappjgis.data.AlbumRepository
import com.example.musicappjgis.ui.UiState
import com.example.musicappjgis.ui.components.MiniPlayer
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme
import com.example.musicappjgis.ui.theme.PurpleLight
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(albumId: String) {
    val state = remember { mutableStateOf(UiState<Album>(loading = true)) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(albumId) {
        val result = AlbumRepository.getAlbum(albumId)
        state.value = result.fold(
            onSuccess = { UiState(loading = false, data = it) },
            onFailure = { UiState(loading = false, error = it.message ?: "Error") }
        )
    }

    fun retry() {
        state.value = UiState(loading = true)
        scope.launch {
            val result = AlbumRepository.getAlbum(albumId)
            state.value = result.fold(
                onSuccess = { UiState(loading = false, data = it) },
                onFailure = { UiState(loading = false, error = it.message ?: "Error") }
            )
        }
    }

    Scaffold(bottomBar = { MiniPlayer() }) { inner ->
        when {
            state.value.loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.value.error != null -> {
                Box(modifier = Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "Algo salió mal")
                        Button(onClick = { retry() }) { Text(text = "Retry") }
                    }
                }
            }
            else -> {
                val album = state.value.data!!
                val tracks = List(10) { index -> "${album.title} • Track ${index + 1}" }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(inner),
                    contentPadding = PaddingValues(bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        DetailHeader(
                            title = album.title,
                            artist = album.artist,
                            imageUrl = album.image
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(text = "About this album", style = MaterialTheme.typography.titleLarge)
                                Text(text = album.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    item {
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            color = Color(0xFFEDE7FF),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "Artist: ${album.artist}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                    items(tracks) { track ->
                        TrackItem(
                            imageUrl = album.image,
                            title = track,
                            artist = album.artist
                        )
                    }
                    item { Spacer(modifier = Modifier.height(4.dp)) }
                }
            }
        }
    }
}

@Composable
fun DetailHeader(title: String, artist: String, imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, PurpleLight.copy(alpha = 0.9f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Text(text = artist, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            DetailActions()
        }
    }
}

@Composable
fun DetailActions() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = {}) { Text(text = "Play") }
        Button(onClick = {}) { Text(text = "Shuffle") }
    }
}

@Composable
fun TrackItem(imageUrl: String, title: String, artist: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, maxLines = 1)
                Text(text = artist, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MusicAppJGISTheme {
        TrackItem(
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/4/42/Beatles_-_Abbey_Road.jpg",
            title = "Tales of Ithiria • Track 1",
            artist = "Haggard"
        )
    }
}
