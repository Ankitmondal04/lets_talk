import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.letstalk.app.data.local.TokenManager
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
    chatViewModel: ChatViewModelImpl,
    tokenManager: TokenManager
) {
    // Store userName once after login so all screens can access it
    var userName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        tokenManager.getUserName()?.let { userName = it }
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = authViewModel,
                onLoginSuccess = { name -> userName = name }
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = authViewModel,
                onRegisterSuccess = { name -> userName = name }
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = homeViewModel,
                userName = userName
            )
        }
        composable("chat/{roomLabel}") { backStackEntry ->
            val roomLabel = backStackEntry.arguments?.getString("roomLabel") ?: return@composable
            ChatScreen(
                navController = navController,
                viewModel = chatViewModel,
                userName = userName,
                roomLabel = roomLabel
            )
        }
    }
}