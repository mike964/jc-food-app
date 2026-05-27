package com.example.gmfastfood.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ==========================================
// 1. AUTH DATA RECOGNITION MODELS
// ==========================================
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

//sealed interface AuthUiState {
//    object Unauthenticated : AuthUiState
//    object Processing : AuthUiState
//    data class Authenticated(val profile: UserProfile) : AuthUiState
//    data class AuthError(val errorMessage: String) : AuthUiState
//}


// ==========================================
// 3. MAIN COMPOSABLE WORKSPACE CONTROLLER
// ==========================================
@Composable
fun AuthFlowAppContainer(authViewModel: AuthViewModel = viewModel()) {
    val activeSession by authViewModel.authState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        // Evaluate app-level routing based dynamically on session parameters
        when (val session = activeSession) {
            is AuthUiState.Unauthenticated -> {
                LoginPanelScreen(onLoginTriggered = { user, pass ->
                    authViewModel.login(
                        user,
                        pass
                    )
                })
            }

            is AuthUiState.Loading -> {
                FullscreenSpinnerOverlay()
            }

            is AuthUiState.Authenticated -> {
                SecureUserDashboard(
                    profile = session.profile,
                    onLogoutTriggered = { authViewModel.logout() })
            }

            is AuthUiState.Error -> {
                // Return login view while explicitly passing along error text block definitions
                LoginPanelScreen(
//                    initialErrorMessage = session.errorMessage,
                    initialErrorMessage = "Error message",
                    onLoginTriggered = { user, pass -> authViewModel.login(user, pass) }
                )
            }
        }
    }
}

// ==========================================
// 4. SUB-VIEW PRESENTATION CORES
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPanelScreen(
    initialErrorMessage: String? = null,
    onLoginTriggered: (String, String) -> Unit,
) {
    var accountUserText by remember { mutableStateOf("") }
    var accountSecretPasswordText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Secure Access Gateway", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                "Sign in to unlock your online e-store panel",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Error Prompt Notification Bar layout container updates
            AnimatedVisibility(
                visible = initialErrorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                initialErrorMessage?.let { message ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.errorContainer,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            message,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // User Field
            OutlinedTextField(
                value = accountUserText,
                onValueChange = { accountUserText = it },
                label = { Text("Username") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Password Field
            OutlinedTextField(
                value = accountSecretPasswordText,
                onValueChange = { accountSecretPasswordText = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onLoginTriggered(accountUserText, accountSecretPasswordText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Authenticate System Identity", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun SecureUserDashboard(profile: UserProfile, onLogoutTriggered: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🛡️", fontSize = 54.sp)
            Text("Vault Verified Successfully!", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("User: ${profile.username}", fontWeight = FontWeight.SemiBold)
                    Text(
                        "Session ID Token: ${profile.email.hashCode()}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogoutTriggered,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Terminate Active Session")
            }
        }
    }
}

@Composable
fun FullscreenSpinnerOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAAFFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Cryptographic Verification Engine running...",
                color = Color.DarkGray,
                fontSize = 14.sp
            )
        }
    }
}