package com.example.musicappjgis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicappjgis.ui.detail.DetailScreen
import com.example.musicappjgis.ui.home.HomeScreen
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppJGISTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(onAlbumClick = { albumId ->
                                navController.navigate("detail/$albumId")
                            })
                        }
                        composable("detail/{albumId}") { backStackEntry ->
                            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
                            DetailScreen(albumId = albumId)
                        }
                    }
                }
            }
        }
    }
}
