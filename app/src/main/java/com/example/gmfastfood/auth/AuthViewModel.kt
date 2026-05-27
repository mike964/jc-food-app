package com.example.gmfastfood.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _sessionState = MutableStateFlow<AuthSessionState>(AuthSessionState.Unauthenticated)
    val sessionState: StateFlow<AuthSessionState> = _sessionState.asStateFlow()

    fun login(usernameInput: String, passwordInput: String) {
        if (usernameInput.isBlank() || passwordInput.isBlank()) {
            _sessionState.update { AuthSessionState.AuthError("Username or password cannot be blank.") }
            return
        }

        viewModelScope.launch {
            // Put UI into loading state
            _sessionState.update { AuthSessionState.Processing }

            // Simulate API roundtrip delay (1.5 seconds)
            delay(1500)

            // Mock check: accepted credential set -> admin / secret123
            if (usernameInput.trim().equals("admin", ignoreCase = true) && passwordInput == "secret123") {
                _sessionState.update {
                    AuthSessionState.Authenticated(
                        profile = UserProfile(  "Alex Mercer", email = "alex@hub.com")
                    )
                }
            } else {
                _sessionState.update { AuthSessionState.AuthError("Invalid credentials. Try: admin / secret123") }
            }
        }
    }

    fun logout() {
        _sessionState.update { AuthSessionState.Unauthenticated }
    }
}