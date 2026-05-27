package com.example.gmfastfood.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.auth.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profile: UserProfile, onLogout: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* Handle edit profile */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)) // Subtle light background grey
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Profile Header Section
//            ProfileHeader(
//                name = "Alex Mercer",
//                email = "alex.mercer@android.com",
//                avatarLabel = "AM"
//            )

            Text("🔐 Access Granted", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("Welcome, ${profile.username}!", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text("Logged in as: ${profile.email}", color = Color.Gray)

            // 2. Performance/Activity Stats Row
            ProfileStatsRow()

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Action / Settings Options List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ProfileMenuItem(icon = Icons.Default.ShoppingCart, label = "Order History") { /* Navigate */ }
                    HorizontalDivider(color = Color(0xFFF1F1F1), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(icon = Icons.Default.Home, label = "My Addresses") { /* Navigate */ }
                    HorizontalDivider(color = Color(0xFFF1F1F1), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(icon = Icons.Default.Notifications, label = "Notification Settings") { /* Navigate */ }
                    HorizontalDivider(color = Color(0xFFF1F1F1), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(icon = Icons.Default.Settings, label = "Account Preferences") { /* Navigate */ }
                    HorizontalDivider(color = Color(0xFFF1F1F1), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))

                    ProfileMenuItem(icon = Icons.Default.Person, label = "Privacy & Security") { /* Navigate */ }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            TextButton(
                onClick = { /* Handle Logout */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Log Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ==========================================
// COMPONENT SUB-VIEWS
// ==========================================

@Composable
fun ProfileHeader(name: String, email: String, avatarLabel: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar Circle Placeholder (Switch to AsyncImage in production for URL loading)
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = avatarLabel,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = email, fontSize = 14.sp, color = Color.Gray)

    }
}

@Composable
fun ProfileStatsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(value = "12", label = "Orders")
        Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color.LightGray)) // Divider
        StatItem(value = "$428", label = "Saved")
        Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color.LightGray)) // Divider
        StatItem(value = "4.9", label = "Buyer Score")
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate Next",
            tint = Color.LightGray
        )
    }
}