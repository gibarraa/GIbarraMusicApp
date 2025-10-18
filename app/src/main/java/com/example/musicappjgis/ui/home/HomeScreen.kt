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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicappjgis.ui.components.MiniPlayer
import com.example.musicappjgis.ui.theme.Background
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme
import com.example.musicappjgis.ui.theme.PurpleLight
import com.example.musicappjgis.ui.theme.TextSecondary

@Composable
fun HomeScreen(onAlbumClick: (String) -> Unit) {
    val demo = listOf("A", "B", "C", "D")
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
                    items(demo) {
                        Card(
                            modifier = Modifier
                                .height(160.dp)
                                .fillMaxWidth(0.72f),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Album")
                            }
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
                    items(demo) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(text = "Item")
                            }
                        }
                    }
                }
                Box(modifier = Modifier.height(72.dp))
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
