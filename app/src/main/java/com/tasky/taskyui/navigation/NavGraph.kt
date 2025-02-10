package com.tasky.taskyui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tasky.taskyui.ui.counter.CounterScreen
import com.tasky.taskyui.ui.login.LoginScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val sharedPrefs = LocalContext.current.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPrefs.getBoolean("is_logged_in", false)
    val startDestination = if (isLoggedIn) "counter" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("counter") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("counter") {
            CounterScreen(onLogout = {
                with(sharedPrefs.edit()) {
                    putBoolean("is_logged_in", false)
                    apply()
                }
                navController.navigate("login") {
                    popUpTo("counter") {
                        inclusive = true
                    }
                }
            })
        }
    }
}