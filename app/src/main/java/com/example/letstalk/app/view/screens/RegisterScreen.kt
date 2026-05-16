package com.example.letstalk.app.view.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.letstalk.app.util.Resource
import com.example.letstalk.app.view.viewmodel.AuthViewModelImpl

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModelImpl,
    onRegisterSuccess: (String) -> Unit
) {
    val context = LocalContext.current

    val userName by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when(authState) {
            is Resource.Success -> {
                onRegisterSuccess(viewModel.userName.value)
                navController.navigate("home") {
                    popUpTo("register") {
                        inclusive = true
                    }
                }
            }
            is Resource.Error -> {
                Toast.makeText(
                    context,
                    (authState as Resource.Error).message ?: "Registration failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Account")
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = userName,
            onValueChange = {viewModel.onUserNameChange(it)},
            label = {Text("Enter Username")},
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = password,
        onValueChange = {viewModel.onPasswordChange(it)},
        label = {Text(text = "Enter Password")},
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(32.dp))
    if(authState is Resource.Loading) {
        CircularProgressIndicator()
    } else {
        Button(
            onClick = {viewModel.register()},
            modifier = Modifier.fillMaxWidth()
        ) { Text(text = "Register")}
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Already have an account? Login",
        modifier = Modifier.clickable {
            navController.navigate("login") {
                popUpTo("register") {
                    inclusive = true
                }
            }
        }
    )
}