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
    @Serializable data object Home : Routes
    @Serializable data object Search : Routes
    @Serializable data object Input : Routes     // Tab 1
    @Serializable data object Display : Routes   // Tab 2
    @Serializable data object Cart : Routes      // Tab 3
    @Serializable data object Checkout : Routes      // Tab 3

    @Serializable data object Orders : Routes   // Tab 4
    @Serializable data object Profile : Routes   // Tab 4
}

