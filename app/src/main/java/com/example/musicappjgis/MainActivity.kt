package com.example.musicappjgis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.musicappjgis.nav.AppNavGraph
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppJGISTheme {
                Surface {
                    AppNavGraph()
                }
            }
        }
    }
}
