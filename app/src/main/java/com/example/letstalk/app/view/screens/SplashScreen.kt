package com.example.letstalk.app.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.letstalk.app.view.viewmodel.AuthViewModelImpl

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AuthViewModelImpl
) {
    LaunchedEffect(Unit) {
        viewModel.checkAuthState().collect { token ->
            if(token != null) {
                navController.navigate("home") {
                    popUpTo("splash") {inclusive = true}
                }
            }else {
                navController.navigate("logiin") {
                    popUpTo("splash") {inclusive = true}
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "LetsTalk")
        CircularProgressIndicator()
    }
}