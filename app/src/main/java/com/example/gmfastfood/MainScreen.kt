package com.example.gmfastfood;

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable;
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
import com.example.gmfastfood.vm.CartViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.profile.ProfileScreen
import com.example.gmfastfood.screens.CheckoutScreen

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
    val cartViewModel = viewModel<CartViewModel>()
    val cartTotalItems = cartViewModel.uiState.collectAsState().value.cartItems.sumOf { it.quantity }

    // # Navigation bar items
    val listOfNavItems = listOf(
        NavItem("Home", Icons.Default.Home, Routes.Home),
        NavItem("Search", Icons.Default.Search, Routes.Search),
//    NavItem("Orders", Icons.AutoMirrored.Filled.List, "orders"),
        NavItem("Cart", ImageVector.vectorResource(id = R.drawable.ic_shopping_cart), Routes.Cart),
        NavItem("Profile", Icons.Default.Person, Routes.Profile)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOfNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { BadgeBox(item.icon , item.label,  cartTotalItems) },
                        label = { Text(text = item.label) },
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
                        }
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

                    HomeScreen(sharedViewModel, cartViewModel)
                }
                composable<Routes.Search> {
                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    SearchScreen(sharedViewModel)
                }
                composable<Routes.Cart> {

                    CartScreen(cartViewModel, onCheckoutClick = { navController.navigate(Routes.Checkout) })
                }
                composable<Routes.Checkout> {
                    CheckoutScreen( onBackClicked = { navController.popBackStack() }, onOrderPlaced = { navController.navigate(Routes.Home) })
                }
                composable<Routes.Profile> {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun BadgeBox(x0: ImageVector, label: String,  cartTotalItems: Int) {
    BadgedBox(badge = {
        if ( label == "Cart" && cartTotalItems > 0)
        Text(  cartTotalItems.toString(), modifier = Modifier.padding(top = 8.dp), color = Color.Red, fontSize = 18.sp)
    }) {
        Icon(imageVector = x0, contentDescription = "badge" ,
            modifier = Modifier.size(28.dp),
//            tint = if (cartTotalItems > 0 && label == "Cart") Orange else Color.DarkGray)
            tint =  Color.DarkGray)
    }
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

//@Preview(showBackground = true)
//@Composable
//fun PreviewNavigationBar (){
//    MainScreen( )
//}