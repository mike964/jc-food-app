package com.example.gmfastfood.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
inline fun <reified VM : ViewModel> NavController.getSharedViewModel(navGraphRoute: Any): VM {
    // Find the back stack entry of the parent navigation graph
    val backStackEntry = remember(this) {
        getBackStackEntry(navGraphRoute)
    }
    // Provide the ViewModel scoped to that parent graph's lifecycle
    return viewModel(viewModelStoreOwner = backStackEntry)
}
enum class SharedTabs(val route: Any, val icon: ImageVector, val label: String) {
    HOME(Routes.MainGraph, Icons.Default.Home, "Home"),
    INPUT(Routes.Input, Icons.Default.Edit, "Input"),
    DISPLAY(Routes.Display, Icons.AutoMirrored.Filled.List, "Display"),
    CART(Routes.Cart, Icons.AutoMirrored.Filled.List, "Cart"),
    PROFILE(Routes.Profile, Icons.Default.Person, "Profile")
}

@Composable
fun SharedNavApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                SharedTabs.entries.forEach { tab ->
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.hasRoute(tab.route::class)
                    } == true

                    NavigationBarItem(
                        selected = isSelected,
                        label = { Text(tab.label) },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        onClick = {
                            navController.navigate(tab.route) {
                                // Clear backstack up to the start tab to avoid accumulating memory
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MainGraph, // Points to the wrapper graph
            modifier = Modifier.padding(innerPadding)
        ) {
            // Nested navigation graph wrapper
            navigation<Routes.MainGraph>(startDestination = Routes.Input) {
                composable<Routes.Input> {
                    // Fetch the Shared ViewModel using our helper function
                    val sharedViewModel: SharedNavigationViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    InputScreen(viewModel = sharedViewModel)
                }

                composable<Routes.Display> {
                    // Fetch the exact same instance of the ViewModel here
                    val sharedViewModel: SharedNavigationViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    DisplayScreen(viewModel = sharedViewModel)
                }
            }
        }
    }
}