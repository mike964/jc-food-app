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
    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Unauthenticated)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    fun login(usernameInput: String, passwordInput: String) {
        if (usernameInput.isBlank() || passwordInput.isBlank()) {
            _authState.update { AuthUiState.Error("Username or password cannot be blank.") }
            return
        }

        viewModelScope.launch {
            // Put UI into loading state
            _authState.update { AuthUiState.Loading }

            // Simulate API round trip delay (1.5 seconds)
            delay(1500)

            // Mock check: accepted credential set -> admin / admin123
            if (usernameInput.trim()
                    .equals("admin", ignoreCase = true) && passwordInput == "admin123"
            ) {
                _authState.update {
                    AuthUiState.Authenticated(
                        profile = UserProfile(
                            "Hasan Ali",
                            email = "hasan@mail.com",
                            phoneNumber = "1234567890",
                            mainAddress = "123 Main St",
                            secondaryAddress = "Apt 4B",
                            createdAt = "2023-09-15",
                            updatedAt = "2023-09-15"
                        )
                    )
                }
            } else {
                _authState.update { AuthUiState.Error("Invalid credentials. Try: admin / admin123") }
            }
        }
    }

    // make app state authenticated by default
    init {
        _authState.update {
            AuthUiState.Authenticated(
                profile = UserProfile(
                    "Hasan Ali",
                    email = "hasan@mail.com",
                    phoneNumber = "1234567890",
                    mainAddress = "123 Main St",
                    secondaryAddress = "Apt 4B",
                    createdAt = "2023-09-15",
                    updatedAt = "2023-09-15"
                )
            )
        }
    }

    fun logout() {
        _authState.update { AuthUiState.Unauthenticated }
    }

    fun clearError() {
        if (_authState.value is AuthUiState.Error) {
            _authState.update { AuthUiState.Unauthenticated }
        }
    }
}