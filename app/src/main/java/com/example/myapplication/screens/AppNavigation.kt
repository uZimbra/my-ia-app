package com.example.myapplication.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.contracts.NavigationContract

@Composable
@RequiresApi(Build.VERSION_CODES.R)
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationContract.HOME_SCREEN) {
        composable(NavigationContract.HOME_SCREEN) { HomeScreen(navController) }
        composable(NavigationContract.HISTORY_SCREEN) { HistoryScreen(navController) }
    }

}