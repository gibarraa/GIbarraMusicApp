package com.example.musicappjgis.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicappjgis.ui.detail.DetailScreen
import com.example.musicappjgis.ui.home.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                onAlbumClick = { id ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("albumArg", AlbumArg(id))
                    navController.navigate(Routes.Detail)
                }
            )
        }
        composable(Routes.Detail) {
            val arg = navController.previousBackStackEntry?.savedStateHandle?.get<AlbumArg>("albumArg")
            DetailScreen(albumId = arg?.albumId ?: "")
        }
    }
}