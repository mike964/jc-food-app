package com.example.gmfastfood.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

// Type-safe routes
@Serializable
sealed interface Routes {
    @Serializable data object MainGraph : Routes // Parent graph route
    @Serializable data object Input : Routes     // Tab 1
    @Serializable data object Display : Routes   // Tab 2
}

// 2. UI metadata linking routes to their visual assets
enum class SharedTabs(val route: Any, val icon: ImageVector, val label: String) {
    INPUT(Routes.Input, Icons.Default.Edit, "Input"),
    DISPLAY(Routes.Display, Icons.AutoMirrored.Filled.List, "Display")
}