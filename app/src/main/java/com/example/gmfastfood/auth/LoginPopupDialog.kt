package com.example.gmfastfood.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ParentStoreWorkspace() {
    var isLoginPopupOpen by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = if (isAuthenticated) "Welcome back, Premium Member! 🎉" else "Guest Session Active",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = {
                    if (isAuthenticated) isAuthenticated = false else isLoginPopupOpen = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAuthenticated) Color.DarkGray else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isAuthenticated) "Log Out" else "Trigger Login Popup")
            }
        }

        // Declarative Modal Popup Switcher
        if (isLoginPopupOpen) {
            LoginPopupDialog(
                onDismissRequest = { isLoginPopupOpen = false },
                onLoginSuccess = {
                    isLoginPopupOpen = false
                    isAuthenticated = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginPopupDialog(
    onDismissRequest: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorLabelText by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    BasicAlertDialog(
        onDismissRequest = { if (!isLoading) onDismissRequest() } // Lock dismiss behavior if API call is processing
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Segment
                Text(
                    text = "Account Verification",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Dynamic Warning Error Label
                AnimatedVisibility(visible = errorLabelText != null) {
                    errorLabelText?.let { errorMsg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(errorMsg, color = MaterialTheme.colorScheme.onErrorContainer, fontSize = 12.sp)
                        }
                    }
                }

                /*
                // Input Field Collection
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; errorLabelText = null },
                //    label = { Text("Username") },
              //      leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    disabled = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorLabelText = null },
                   // label = { Text("Password") },
                 //   leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    disabled = isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                 */

                Spacer(modifier = Modifier.height(4.dp))

                // Interactive Action Footer
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(36.dp).padding(vertical = 4.dp))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text("Cancel", color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (username.isBlank() || password.isBlank()) {
                                    errorLabelText = "Fields cannot be left blank."
                                    return@Button
                                }

                                coroutineScope.launch {
                                    isLoading = true
                                    delay(1500) // Simulated backend authorization network latency

                                    if (username.trim() == "admin" && password == "1234") {
                                        onLoginSuccess()
                                    } else {
                                        isLoading = false
                                        errorLabelText = "Incorrect. Use: admin / 1234"
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}