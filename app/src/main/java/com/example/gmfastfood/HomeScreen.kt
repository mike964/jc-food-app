package com.example.gmfastfood

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.data.FakeApiClient
import com.example.gmfastfood.vm.SharedViewModel
import com.example.gmfastfood.vm.CartViewModel
import com.example.gmfastfood.vm.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SharedViewModel,
    cartViewModel: CartViewModel,
    onCheckoutClick: () -> Unit,
    onSubmitOrderClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val text by viewModel.sharedText.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var targetA by remember { mutableFloatStateOf(0f) }
    var burgersPosition by remember { mutableFloatStateOf(0f) }
    var pizzaPosition by remember { mutableFloatStateOf(0f) }
    var drinksPosition by remember { mutableFloatStateOf(0f) }
    var saladsPosition by remember { mutableFloatStateOf(0f) }

//    Log.d("HomeScreen", "burgerPosition: $burgersPosition, pizzaPosition: $pizzaPosition, drinksPosition: $drinksPosition, saladsPosition: $saladsPosition")


    val fakeApi: FakeApiClient = FakeApiClient()

    val state by viewModel.uiState.collectAsState()

    Log.d("HomeScreen", "$state")
    // Success(items=[Product(id=1, title=Cheese Burger, ..

    // Material 3 bottom sheet structural state variables
    var showCartBottomSheet by remember { mutableStateOf(false) }
    val totalCartItems: Int =
        cartViewModel.uiState.collectAsState().value.cartItems.sumOf { it.quantity }
    val cartItems = cartViewModel.uiState.collectAsState().value.cartItems

    var showSearchPopup by remember { mutableStateOf(false) }

    var filterButtons by remember {
        mutableStateOf(
            listOf<String>(
                "All",
                "Burgers",
                "Pizza",
                "Salads",
                "Drinks",
                "Snacks"
            )
        )
    }
    // burgers, pizza, salads, drinks, snacks
    val activeFilters = remember { mutableStateListOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
//            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Overlay Popup Screen Definition
            SearchPopupScreen(
                isOpen = showSearchPopup,
                onDismiss = { showSearchPopup = false },
                onConfirm = {
                    // Execute background tasks or repository updates here
                    println("User clicked confirmed!")
                },
                viewModel = viewModel,
                cartViewModel = cartViewModel
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Column(
                    Modifier
//                        .background(Color(0xFFF1CACA))
//                        .height(200.dp)
                ) {
                    //======================
                    // # Header
                    //======================
                    Row(
                        Modifier
                            .background(Color(0xFFE82020))
                            .padding(10.dp)
                    ) {
                        Column(
                            Modifier
                                .weight(2f)
                                .padding(start = 8.dp)
                        ) {
                            Text(
                                "Food & More",
                                Modifier,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF3D0606),
                            )
                            Text(
                                "Enjoy your favourite food!", Modifier, fontSize = 15.sp,
                                color = Color(0xFFEA7A7A),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Column(
                            Modifier.weight(1f)
                            // .background(Color.Yellow)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, end = 8.dp),
                                horizontalArrangement = Arrangement.End, // Horizontal centering
                                verticalAlignment = Alignment.CenterVertically // Vertical centering
                            ) {
                                IconButton(onClick = { showSearchPopup = true }) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Open Search popup window",
                                        Modifier.size(38.dp)
                                    )
                                }
                                // Navigate to Profile Screen
                                IconButton(onClick = {
                                    onProfileClick()
                                }) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Open Search popup window",
                                        Modifier.size(38.dp)
                                    )
                                }
                            }
                        }
                    }

                    // # Only if cart has one item or more show this row with total price
                    if (cartViewModel.uiState.collectAsState().value.cartItems.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    Color(0xFFD6EAD6)
                                )
                        ) {
                            Column(
                                Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                IconButton(
                                    onClick = { showCartBottomSheet = true },
//                                    modifier = Modifier.background(Color.LightGray, shape = RectangleShape)
                                ) {
                                    BadgedBox(
                                        modifier = Modifier.padding(top = 8.dp),
                                        badge = {
                                            if (totalCartItems > 0)
                                                Badge { Text(totalCartItems.toString()) }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.ShoppingCart,
                                            contentDescription = "Open Bag Window View",
                                            Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }
                            Column(
                                Modifier
                                    .weight(2f)
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(
                                    onClick = {
                                        onSubmitOrderClick()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2FB235), // Background color
                                        contentColor = Color.White  // Text/Icon color
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text("Submit Order", fontSize = 18.sp)
                                }
                            }
                            Column(
                                Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Total : ${cartViewModel.uiState.collectAsState().value.total.toInt()}")
                            }
                        }
                    }

                    // 3. DECLARATIVE MODAL BOTTOM SHEET OVERLAY CONTEXT
                    if (showCartBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showCartBottomSheet = false },
                            dragHandle = { BottomSheetDefaults.DragHandle() },
                            containerColor = Color.White
                        ) {
                            ShoppingCartContent(
                                cartItems = cartViewModel.uiState.collectAsState().value.cartItems,
                                onUpdateQuantity = { itemId, newQty ->
                                    cartViewModel.updateQuantity(itemId, newQty)
                                },
                                onCheckoutClicked = {
                                    showCartBottomSheet = false
                                    /* Navigate to Checkout Screen */
                                    onCheckoutClick()
                                }
                            )
                        }
                    }
                    //======================================
                    // # Horizontal list of filter buttons
                    //======================================
                    LazyRow(
                        // Adds spacing between elements
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        // Adds padding at the start and end of the scrolling bounds
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        items(filterButtons) { item ->
                            FilterButton(item, activeFilters.contains(item)
                            ) {
                                if (activeFilters.contains(item))
                                    activeFilters.remove(item)
                                else
                                    activeFilters.add(item)
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                }
            }

//        HorizontalCardList(itemList = listOf("Burgers", "Pizza", "Sushi", "Drinks", "Desserts", "Salads", "Pasta", "Snacks", "Soup"))
//        Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                //  .fillMaxSize()
//                    .background(Color.Cyan)
//                    .height(600.dp)
            ) {
                Column(
                    modifier = Modifier
                        //  .fillMaxSize()
//            .background(Color.Cyan)
                        .verticalScroll(scrollState)
//                        .padding(top = 100.dp) // Offset by the row's height
                ) {
                    Text(
                        "Popular", Modifier.padding(16.dp, 4.dp),
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )

                    when (val currentState = state) {
                        is UiState.Loading -> {
                            Box(Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(strokeWidth = 4.dp)
                                Spacer(modifier = Modifier.height(12.dp))
//                   Text("Simulating fetch request (2.5s)...", color = Color.Gray, fontSize = 14.sp)
                            }
                        }

                        is UiState.Success -> {
                            HorizontalCardList(
                                itemList = currentState.items,
                                addToCart = { cartViewModel.addToCart(it) },
                                onUpdateQuantity = { itemId, newQty ->
                                    cartViewModel.updateQuantity(itemId, newQty)
                                },
                                cartItems
                            )
                        }

                        is UiState.Error -> {
                            Text(currentState.message, color = Color.Red, fontSize = 14.sp)
                        }
                    }

                    Text(
                        "Burgers", Modifier
                            .padding(16.dp, 4.dp)
                            .onGloballyPositioned { coordinates ->
                                burgersPosition = coordinates.positionInRoot().y
                            },
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    HorizontalCardList(
                        itemList = fakeApi.getProductsByCategory("Burgers"),
                        addToCart = { cartViewModel.addToCart(it) },
                        onUpdateQuantity = { itemId, newQty ->
                            cartViewModel.updateQuantity(itemId, newQty)
                        },
                        cartItems
                    )

                    Text(
                        "Pizza", Modifier
                            .padding(16.dp, 4.dp)
                            .onGloballyPositioned { coordinates ->
                                pizzaPosition = coordinates.positionInRoot().y
                            },
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    HorizontalCardList(
                        itemList = fakeApi.getProductsByCategory("Pizza"),
                        addToCart = { cartViewModel.addToCart(it) },
                        onUpdateQuantity = { itemId, newQty ->
                            cartViewModel.updateQuantity(itemId, newQty)
                        },
                        cartItems
                    )

                    Text(
                        "Salads", Modifier
                            .padding(16.dp, 4.dp)
                            .onGloballyPositioned { coordinates ->
                                saladsPosition = coordinates.positionInRoot().y
                            },
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    HorizontalCardList(
                        itemList = fakeApi.getProductsByCategory("Salads"),
                        addToCart = { cartViewModel.addToCart(it) },
                        onUpdateQuantity = { itemId, newQty ->
                            cartViewModel.updateQuantity(itemId, newQty)
                        },
                        cartItems
                    )

                    Text(
                        "Drinks", Modifier
                            .padding(16.dp, 4.dp)
                            .onGloballyPositioned { coordinates ->
                                drinksPosition = coordinates.positionInRoot().y
                            },
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    HorizontalCardList(
                        itemList = fakeApi.getProductsByCategory("Drinks"),
                        addToCart = { cartViewModel.addToCart(it) },
                        onUpdateQuantity = { itemId, newQty ->
                            cartViewModel.updateQuantity(itemId, newQty)
                        },
                        cartItems
                    )

                    Spacer(Modifier.height(600.dp))
                }
            }
        }
    }
}


data class SlidingContent(
    val text: String,
    val selected: Boolean = false,
)
