package com.example.musicappjgis.ui.detail

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicappjgis.data.Album
import com.example.musicappjgis.data.AlbumRepository
import com.example.musicappjgis.ui.UiState
import com.example.musicappjgis.ui.components.MiniPlayer
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme
import kotlinx.coroutines.launch
import kotlin.math.min

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

    val current = state.value.data
    val miniImage = current?.image ?: ""
    val miniTitle = current?.title ?: "Now Playing"
    val miniArtist = current?.artist ?: "..."

    Scaffold(
        bottomBar = { Box(modifier = Modifier.navigationBarsPadding()) { MiniPlayer(imageUrl = miniImage, title = miniTitle, artist = miniArtist) } }
    ) { inner ->
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
                val listState = rememberLazyListState()
                val maxHeader = 320.dp
                val minHeader = 120.dp
                val density = LocalDensity.current
                val maxPx = with(density) { maxHeader.toPx() }
                val minPx = with(density) { minHeader.toPx() }
                val scrollOffsetPx by remember {
                    derivedStateOf {
                        val firstIndex = listState.firstVisibleItemIndex
                        val firstOffset = listState.firstVisibleItemScrollOffset
                        if (firstIndex > 0) maxPx - minPx else min(firstOffset.toFloat(), maxPx - minPx)
                    }
                }
                val collapse = (scrollOffsetPx / (maxPx - minPx)).coerceIn(0f, 1f)
                val headerHeight: Dp = with(density) { (maxPx - collapse * (maxPx - minPx)).toDp() }
                val titleTopAlpha by animateFloatAsState(targetValue = collapse)
                val headerContentAlpha by animateFloatAsState(targetValue = 1f - collapse)

                Box(modifier = Modifier.fillMaxSize().padding(inner)) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { Spacer(modifier = Modifier.height(maxHeader)) }
                        item {
                            Card(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "About this album", style = MaterialTheme.typography.titleLarge)
                                    Text(text = album.description, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                        item {
                            Surface(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFFEDE7FF),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "Artist: ${album.artist}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                        items(List(10) { i -> "${album.title} • Track ${i + 1}" }) { track ->
                            TrackItemPretty(imageUrl = album.image, title = track, artist = album.artist)
                        }
                        item { Spacer(modifier = Modifier.height(4.dp)) }
                    }
                    CollapsingHeaderPretty(
                        title = album.title,
                        artist = album.artist,
                        imageUrl = album.image,
                        height = headerHeight,
                        contentAlpha = headerContentAlpha
                    )
                    TopOverlayTitlePretty(title = album.title, alpha = titleTopAlpha)
                }
            }
        }
    }
}

@Composable
fun CollapsingHeaderPretty(title: String, artist: String, imageUrl: String, height: Dp, contentAlpha: Float) {
    val shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0x00000000),
                            Color(0x802C105C),
                            Color(0xCC2C105C)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .alpha(contentAlpha),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artist,
                color = Color(0xFFE4D8FF),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {},
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6F44FF), contentColor = Color.White)
                ) { Text(text = "Play") }
                Button(
                    onClick = {},
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF6F44FF))
                ) { Text(text = "Shuffle") }
            }
        }
    }
}

@Composable
fun TopOverlayTitlePretty(title: String, alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0x992C105C), Color(0x552C105C), Color.Transparent)
                )
            )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .alpha(alpha)
        )
    }
}

@Composable
fun TrackItemPretty(imageUrl: String, title: String, artist: String) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(22.dp)
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
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = artist, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF6B6B6B), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreviewHeaderOnly() {
    MusicAppJGISTheme {
        CollapsingHeaderPretty(
            title = "Tales of Ithiria",
            artist = "Haggard",
            imageUrl = "https://upload.wikimedia.org/wikipedia/en/4/42/Beatles_-_Abbey_Road.jpg",
            height = 320.dp,
            contentAlpha = 1f
        )
    }
}
