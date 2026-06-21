package com.example.gmfastfood;

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable;
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
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
import com.example.gmfastfood.vm.SharedViewModel
import com.example.gmfastfood.vm.CartViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.toRoute
import com.example.gmfastfood.auth.AuthFlowContainer
import com.example.gmfastfood.auth.AuthViewModel
import com.example.gmfastfood.checkout.CheckoutScreen
import com.example.gmfastfood.checkout.OrderDetailsScreen
import com.example.gmfastfood.extra.SearchScreen2
import com.example.gmfastfood.home.HomeScreen
import com.example.gmfastfood.order.OrdersScreen
import com.example.gmfastfood.profile.AddressListScreen

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
    val authViewModel = viewModel<AuthViewModel>()
    val cartTotalItems =
        cartViewModel.uiState.collectAsState().value.cartItems.sumOf { it.quantity }

    // # Navigation bar items
    val listOfNavItems = listOf(
        NavItem("Home", Icons.Default.Home, Routes.Home),
//        NavItem("Search", Icons.Default.Search, Routes.Search),
        NavItem("Cart", ImageVector.vectorResource(id = R.drawable.ic_shopping_cart), Routes.Cart),
        NavItem("Orders", Icons.AutoMirrored.Filled.List, Routes.Orders),
        NavItem("Profile", Icons.Default.Person, Routes.Profile)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOfNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { BadgeBox(item.icon, item.label, cartTotalItems) },
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

                    HomeScreen(
                        sharedViewModel, cartViewModel,
                        onCheckoutClick = { navController.navigate(Routes.Checkout) },
                        onSubmitOrderClick = { navController.navigate(Routes.Cart) },
                        onProfileClick = { navController.navigate(Routes.Profile) }
                    )
                }
                composable<Routes.Cart> {
                    CartScreen(
                        cartViewModel,
                        onCheckoutClick = { navController.navigate(Routes.Checkout) },
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable<Routes.Checkout> {


                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    fun navigateToOrderDetails(orderId: String) {
                        navController.navigate(Routes.OrderDetails(orderId = orderId))
                    }

                    CheckoutScreen(
                        viewModel = sharedViewModel,
                        cartViewModel = cartViewModel,
                        onBackClicked = { navController.popBackStack() },
                        navigateToOrderDetails = { orderId ->
                            navigateToOrderDetails(orderId)
                        }
                    )
                }
                composable<Routes.OrderDetails> { backStackEntry ->
                    // Extract the arguments automatically and type-safely
                    val args = backStackEntry.toRoute<Routes.OrderDetails>()

                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)

                    val order = sharedViewModel.getOrderById(args.orderId)

                    OrderDetailsScreen(
                        order = order,
                        onBackClick = { navController.popBackStack() },
                        onHomeClick = { navController.navigate(Routes.Home) },
                        onMyOrdersClick = { navController.navigate(Routes.Orders) }
                    )
                }
                composable<Routes.Orders> {
                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)
                    OrdersScreen(
                        onBackClick = { navController.popBackStack() },
                        viewModel = sharedViewModel
                    )
                }
                composable<Routes.Addresses> {
                    val sharedViewModel: SharedViewModel =
                        navController.getSharedViewModel(Routes.MainGraph)
//                    AddressEditScreen( initialAddress = null, onBackClick = { navController.popBackStack() }, onSaveClick = { })
                    AddressListScreen(
                        viewModel = sharedViewModel,
                        selectedAddressId = null,
                        onBackClick = { navController.popBackStack() },
                        onAddressSelect = { },
                        onEditClick = { },
                        onAddNewAddressClick = { })
                }
//                composable<Routes.Orders> {
//                    val sharedViewModel: SharedViewModel =
//                        navController.getSharedViewModel(Routes.MainGraph)
//                    OrdersScreen2(   onOrderClick = { }, onTrackOrderClick = { } )
//                }
                composable<Routes.Profile> {
                    AuthFlowContainer(authViewModel, navController)
//                    LoginPanelScreen( onLoginTriggered = { user, pass -> } )
//                    LoginScreen( onLoginSubmitted = { user, pass -> navController.navigate(Routes.Home) } )
//                    MainCatalogScreenWithCart()
//                    LoginPopup( onDismiss = { }, isOpen = true )

                }
            }
        }
    }
}

@Composable
fun BadgeBox(x0: ImageVector, label: String, cartTotalItems: Int) {
    BadgedBox(badge = {
        if (label == "Cart" && cartTotalItems > 0)
            Text(
                cartTotalItems.toString(),
//            modifier = Modifier.padding(top = 8.dp ) ,
//                .background(Color.White),
                color = Color.Red,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
    }) {
        Icon(
            imageVector = x0, contentDescription = "badge",
            modifier = Modifier.size(28.dp),
//            tint = if (cartTotalItems > 0 && label == "Cart") Orange else Color.DarkGray)
            tint = Color.DarkGray
        )
    }
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