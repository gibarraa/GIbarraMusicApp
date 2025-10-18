package com.example.musicappjgis.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicappjgis.ui.theme.BottomBarDark

@Composable
fun MiniPlayer() {
    val playing = remember { mutableStateOf(false) }
    Surface(color = BottomBarDark, contentColor = MaterialTheme.colorScheme.onPrimary) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tales of Ithiria • Haggard", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { playing.value = !playing.value }) {
                Text(text = if (playing.value) "⏸" else "▶")
            }
        }
    }
}
