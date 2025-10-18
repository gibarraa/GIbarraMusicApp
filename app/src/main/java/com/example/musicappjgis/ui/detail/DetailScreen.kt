package com.example.musicappjgis.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme

@Composable
fun DetailScreen(albumId: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Detail: $albumId")
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MusicAppJGISTheme {
        DetailScreen(albumId = "preview-id")
    }
}
