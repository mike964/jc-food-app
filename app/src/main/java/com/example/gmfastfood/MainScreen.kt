package com.example.gmfastfood;

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable;
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.gmfastfood.cart.CartScreen
import com.example.gmfastfood.navigation.Routes
import com.example.gmfastfood.navigation.SharedViewModel
import com.example.gmfastfood.navigation.getSharedViewModel

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



@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedIndex by remember { mutableIntStateOf(0) }


    Scaffold(
        bottomBar = {
            NavigationBar {
                listOfNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when re selecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(text = item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost handles switching between screen composables
        NavHost(
            navController = navController,
            startDestination = Routes.MainGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<Routes.MainGraph>(startDestination = Routes.Home) {
                composable<Routes.Home> {
                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    HomeScreen(sharedViewModel)
                }
                composable<Routes.Search> {
                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    SearchScreen(sharedViewModel)
                }
                composable<Routes.Cart> {
                    CartScreen()
                }
                composable<Routes.Profile> {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Text("Profile Screen")
}


@Composable
fun OrdersScreen() {
    Text("Orders")
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any,
)

val listOfNavItems = listOf(
    NavItem("Home", Icons.Default.Home, Routes.Home),
    NavItem("Search", Icons.Default.Search, Routes.Search),
//    NavItem("Orders", Icons.AutoMirrored.Filled.List, "orders"),
    NavItem("Cart", Icons.AutoMirrored.Filled.List, Routes.Cart),
    NavItem("Profile", Icons.Default.Person, Routes.Profile)
)
