package com.example.gmfastfood.extra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Root Graphs
@Serializable object AuthGraph
@Serializable object MainGraph

// Auth Screens
@Serializable object LoginScreen
@Serializable object SignUpScreen

// Main App Screens
@Serializable object HomeScreen
@Serializable object ProfileScreen
// Root Routes
@Serializable
object GraphHome
@Serializable
object GraphCheckout // The Nested Graph Route

// Checkout Sub-Screens
@Serializable
object CartOverview
@Serializable
object ShippingAddress
@Serializable
object OrderSummary

// The Shared State Holder
data class CheckoutState(
    val itemsCount: Int = 3,
    val totalAmount: Double = 89.99,
    val shippingAddress: String = "",
)


class SharedCheckoutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutState())
    val uiState = _uiState.asStateFlow()

    fun updateAddress(newAddress: String) {
        _uiState.update { it.copy(shippingAddress = newAddress) }
    }
}


@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Dynamically set where the user starts
        startDestination = if (isLoggedIn) MainGraph else AuthGraph
    ) {
        // Attach our nested graphs
//        authGraph(navController)
//        mainGraph(navController)
    }
}

/**
 * A safe helper extension to find the parent destination block
 * without crashing or throwing standard IllegalArgumentExceptions.
 */
@Composable
fun rememberParentEntry(
    navController: NavHostController,
    currentEntry: NavBackStackEntry,
): NavBackStackEntry {
    return remember(currentEntry) {
        // GraphCheckout matches the serialization object used in navigation<GraphCheckout>
        navController.getBackStackEntry<GraphCheckout>()
    }
}

// # Nested Navigation Example
@Composable
fun NestedNavigationExample( ) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.AuthRoute.route
    ) {
        navigation(startDestination = Screens.Login.route, route = Screens.AuthRoute.route) {
            composable(Screens.Login.route) {
                val sharedViewModel: SharedViewModel = it.sharedViewModel(navController)
                LoginScreen(navController)
            }
            composable("Register") {
                val sharedViewModel: SharedViewModel = it.sharedViewModel(navController)
                RegisterScreen(navController)
            }
            composable("ForgotPassword") {
                val sharedViewModel: SharedViewModel = it.sharedViewModel(navController)
                ForgotPassword(navController)
            }
        }

        navigation(startDestination = Screens.Home.route , route = Screens.AppRoute.route){
            composable(Screens.Home.route) {
                val sharedViewModel: SharedViewModel = it.sharedViewModel(navController)
                HomeScreen(navController)
            }
            composable(Screens.ScreenA.route) {
                val sharedViewModel: SharedViewModel = it.sharedViewModel(navController)
                ScreenA(navController)
            }
            composable(Screens.ScreenB.route) {
                val sharedViewModel: SharedViewModel = it.sharedViewModel(navController)
                ScreenB(navController)
            }

        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

class SharedViewModel : ViewModel() {
    private val _sharedText = MutableStateFlow("Initial Text")
    val sharedText = _sharedText.asStateFlow()
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screens.ScreenA.route) }) {
            Text("Go to Screen A")
        }
        Button(onClick = { navController.navigate(Screens.ScreenB.route) }) {
            Text("Go to Screen B")
        }
    }
}

@Composable
fun ScreenA(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screens.ScreenB.route) }) {
            Text("Go to Screen B")
        }
        Button(onClick = { navController.navigate(Screens.Register.route) }) {
            Text("Go to Register")
        }
    }
}

@Composable
fun ScreenB(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(Screens.ScreenA.route) }) {
            Text("Go to Screen A")
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login Screen", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate(Screens.Home.route) }) {
            Text("Go to Home")
        }
        Button(onClick = { navController.navigate(Screens.ForgotPassword.route) }) {
            Text("Forgot password")
        }
        Button(onClick = { navController.navigate(Screens.Register.route) }) {
            Text("Register")
        }
    }
}

@Composable
fun ForgotPassword(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Forgot password screen", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate(Screens.Login.route) }) {
            Text("Login")
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register Screen", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navController.navigate(Screens.Login.route) }) {
            Text("Login")
        }
    }
}

sealed class Screens(val route: String) {
    object Home : Screens("Home")
    object ScreenA : Screens("ScreenA")
    object ScreenB : Screens("ScreenB")
    object Login : Screens("Login")
    object ForgotPassword : Screens("ForgotPassword")
    object Register : Screens("Register")
    object AuthRoute : Screens("Auth")
    object AppRoute : Screens("App")
}