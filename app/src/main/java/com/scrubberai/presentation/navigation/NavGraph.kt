package com.scrubberai.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scrubberai.presentation.ui.screens.HomeScreen
import com.scrubberai.presentation.ui.screens.DuplicatesScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(onNavigateDuplicates = { navController.navigate("duplicates") }) }
        composable("duplicates") { DuplicatesScreen(onBack = { navController.popBackStack() }) }
    }
}
