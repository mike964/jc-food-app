package com.example.gmfastfood.auth

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gmfastfood.LoginPopup
import com.example.gmfastfood.profile.ProfileScreen

data class UserProfile(
    val username: String,
    val email: String, val phoneNumber: String? = null,
    val mainAddress: String? = null,
    val secondaryAddress: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

sealed interface AuthUiState {
    object Unauthenticated : AuthUiState
    object Loading : AuthUiState
    data class Authenticated(val profile: UserProfile) : AuthUiState
    data class Error(val message: String) : AuthUiState
}




@Composable
fun AuthFlowContainer(viewModel: AuthViewModel = viewModel()) {
    val activeState by viewModel.authState.collectAsState()

    Log.d("AuthFlowContainer", "Active State: $activeState")
// Active State: com.example.gmfastfood.auth.AuthUiState$Unauthenticated@47e15e8

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA) // Modern, clean light gray canvas background
    ) {
        when (val state = activeState) {
            is AuthUiState.Unauthenticated -> {
//                LoginScreen(onLoginSubmitted = { user, pass -> viewModel.login(user, pass) }, authenticated   = false)
                ProfileScreen( profile = null, isAuthenticated = false,
                    onLoginSubmitted = { user, pass -> viewModel.login(user, pass) },
                    onLogout = { viewModel.logout() } )

            }

            is AuthUiState.Loading -> {
                LoadingOverlay()
            }

            is AuthUiState.Authenticated -> {
//                DashboardScreen(profile = state.profile, onLogout = { viewModel.logout() })
                ProfileScreen( profile = state.profile, isAuthenticated = true,
                    onLoginSubmitted = { user, pass -> viewModel.login(user, pass) },
                    onLogout = { viewModel.logout() },)
            }

            is AuthUiState.Error -> {
                LoginScreen(
                    errorMessage = state.message,
                    onLoginSubmitted = { user, pass -> viewModel.login(user, pass) },
                    authenticated = false
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    errorMessage: String? = null,
    onLoginSubmitted: (String, String) -> Unit,
    authenticated: Boolean,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val viewModel: AuthViewModel = viewModel()
    var loginError by remember { mutableStateOf(authenticated) }
    var loginPopupIsOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(onClick = { loginPopupIsOpen = true }) {
                Text("Log in")
            }

//            LoginPopup(
//                isOpen = loginPopupIsOpen,
//            ) { loginPopupIsOpen = false }

            Text("Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                "Sign in to access your digital marketplace profile",
                color = Color.Gray,
                fontSize = 14.sp
            )

            // Handle Error state seamlessly with smooth fade animations
            AnimatedVisibility(visible = errorMessage != null) {
                errorMessage?.let { msg ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            msg,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { onLoginSubmitted(username, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign In", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun DashboardScreen(profile: UserProfile, onLogout: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🔐 Access Granted", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("Welcome, ${profile.username}!", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text("Logged in as: ${profile.email}", color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Log Out Session")
            }
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text("Authorizing token credentials...", color = Color.Gray, fontSize = 14.sp)
        }
    }
}