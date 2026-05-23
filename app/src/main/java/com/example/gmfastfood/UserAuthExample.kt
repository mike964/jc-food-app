package com.example.gmfastfood

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
data class UserSessionProfile(val username: String, val email: String)

sealed interface AuthSessionState {
    object Unauthenticated : AuthSessionState
    object Processing : AuthSessionState
    data class Authenticated(val profile: UserSessionProfile) : AuthSessionState
    data class AuthenticationError(val errorMessage: String) : AuthSessionState
}

// ==========================================
// 2. THE AUTH VIEWMODEL STATE MACHINE
// ==========================================
class SessionAuthViewModel : ViewModel() {

    private val _sessionState = MutableStateFlow<AuthSessionState>(AuthSessionState.Unauthenticated)
    val sessionState: StateFlow<AuthSessionState> = _sessionState.asStateFlow()

    fun attemptLoginUser(usernameInput: String, passwordInput: String) {
        if (usernameInput.isBlank() || passwordInput.isBlank()) {
            _sessionState.update { AuthSessionState.AuthenticationError("Input fields cannot be empty.") }
            return
        }

        viewModelScope.launch {
            // 1. Set background processing status overlay spinner
            _sessionState.update { AuthSessionState.Processing }

            // 2. Simulate standard cloud server validation response lag (1.8 seconds)
            delay(1800)

            // 3. Simple static credentials validation logic check
            if (usernameInput.equals("admin", ignoreCase = true) && passwordInput == "password123") {
                _sessionState.update {
                    AuthSessionState.Authenticated(
                        profile = UserSessionProfile(username = "Alex Mercer", email = "alex.mercer@android.com")
                    )
                }
            } else {
                _sessionState.update { AuthSessionState.AuthenticationError("Invalid credentials. Try: admin / password123") }
            }
        }
    }

    fun requestUserLogout() {
        _sessionState.update { AuthSessionState.Unauthenticated }
    }
}

// ==========================================
// 3. MAIN COMPOSABLE WORKSPACE CONTROLLER
// ==========================================
@Composable
fun AuthFlowAppContainer(authViewModel: SessionAuthViewModel = viewModel()) {
    val activeSession by authViewModel.sessionState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        // Evaluate app-level routing based dynamically on session parameters
        when (val session = activeSession) {
            is AuthSessionState.Unauthenticated -> {
                LoginPanelScreen(onLoginTriggered = { user, pass -> authViewModel.attemptLoginUser(user, pass) })
            }
            is AuthSessionState.Processing -> {
                FullscreenSpinnerOverlay()
            }
            is AuthSessionState.Authenticated -> {
                SecureUserDashboard(profile = session.profile, onLogoutTriggered = { authViewModel.requestUserLogout() })
            }
            is AuthSessionState.AuthenticationError -> {
                // Return login view while explicitly passing along error text block definitions
                LoginPanelScreen(
                    initialErrorMessage = session.errorMessage,
                    onLoginTriggered = { user, pass -> authViewModel.attemptLoginUser(user, pass) }
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
    onLoginTriggered: (String, String) -> Unit
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
            Text("Sign in to unlock your online e-store panel", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            // Error Prompt Notification Bar layout container updates
            AnimatedVisibility(visible = initialErrorMessage != null, enter = fadeIn(), exit = fadeOut()) {
                initialErrorMessage?.let { message ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(message, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 13.sp)
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Authenticate System Identity", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun SecureUserDashboard(profile: UserSessionProfile, onLogoutTriggered: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("🛡️", fontSize = 54.sp)
            Text("Vault Verified Successfully!", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("User: ${profile.username}", fontWeight = FontWeight.SemiBold)
                    Text("Session ID Token: ${profile.email.hashCode()}", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLogoutTriggered, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                Text("Terminate Active Session")
            }
        }
    }
}

@Composable
fun FullscreenSpinnerOverlay() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xAAFFFFFF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cryptographic Verification Engine running...", color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}