package com.example.letstalk.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.letstalk.app.view.screens.ChatScreen
import com.example.letstalk.app.view.screens.HomeScreen
import com.example.letstalk.app.view.screens.LoginScreen
import com.example.letstalk.app.view.screens.RegisterScreen
import com.example.letstalk.app.view.screens.SplashScreen
import com.example.letstalk.app.view.viewmodel.AuthViewModelImpl
import com.example.letstalk.app.view.viewmodel.ChatViewModelImpl
import com.example.letstalk.app.view.viewmodel.HomeViewModelImpl

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModelImpl,
    homeViewModel: HomeViewModelImpl,
    chatViewModel: ChatViewModelImpl
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController, authViewModel)
        }
        composable("register") {
            RegisterScreen(navController, authViewModel)
        }
        composable("login") {
            LoginScreen(navController, authViewModel)
        }
        composable("home") {
            HomeScreen(navController, homeViewModel)
        }
        composable("chat/{roomLabel}") { backStackEntry ->
            val roomLabel = backStackEntry.arguments?.getString("roomLabel") ?: return@composable
            ChatScreen() //navController and roomLabel in the constructor
        }
    }
}