package com.example.musicappjgis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.musicappjgis.nav.AlbumArg
import com.example.musicappjgis.ui.detail.DetailScreen
import com.example.musicappjgis.ui.home.HomeScreen
import com.example.musicappjgis.ui.theme.MusicAppJGISTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppJGISTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberAnimatedNavController()
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "home",
                        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
                    ) {
                        composable("home") {
                            HomeScreen(onAlbumClick = { albumId ->
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "albumArg",
                                    AlbumArg(albumId)
                                )
                                navController.navigate("detail")
                            })
                        }
                        composable("detail") {
                            val arg = navController.previousBackStackEntry?.savedStateHandle?.get<AlbumArg>("albumArg")
                            DetailScreen(albumId = arg?.albumId ?: "")
                        }
                    }
                }
            }
        }
    }
}
