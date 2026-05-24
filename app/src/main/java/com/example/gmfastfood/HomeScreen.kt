package com.example.gmfastfood

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.data.FakeApiClient
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.vm.SharedViewModel
import com.example.gmfastfood.vm.CartItem
import com.example.gmfastfood.vm.CartViewModel
import com.example.gmfastfood.vm.UiState

@Composable
fun HomeScreen(viewModel: SharedViewModel, cartViewModel: CartViewModel) {
    val text by viewModel.sharedText.collectAsState()
    val scrollState = rememberScrollState()

    val fakeApi : FakeApiClient = FakeApiClient()

    val state by viewModel.uiState.collectAsState()

    Log.d("HomeScreen", "$state")
    // Success(items=[Product(id=1, title=Cheese Burger, ..


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
//            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            Text("Food & More", Modifier, fontSize = 38.sp, fontWeight = FontWeight.SemiBold)
            Text(
                "Order your favourite food!", Modifier, fontSize = 15.sp,
                color = Color(0xFF868C96),
                fontWeight = FontWeight.Medium
            )
            SearchBox(textValue = text, onValueChange = { viewModel.updateText(it) })
//        HorizontalSlider()
        }

        HorizontalList(
            itemsList = listOf(
                "Burgers",
                "Pizza",
                "Drinks",
                "Desserts",
                "Salads",
                "Pasta",
                "Snacks",
                "Soup"
            )
        )

//        HorizontalCardList(itemList = listOf("Burgers", "Pizza", "Sushi", "Drinks", "Desserts", "Salads", "Pasta", "Snacks", "Soup"))
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Popular", Modifier.padding(16.dp, 4.dp),
            fontSize = 18.sp, fontWeight = FontWeight.SemiBold
        )

        when (val currentState = state) {
            is UiState.Loading -> {
                Box( Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 4.dp)
                    Spacer(modifier = Modifier.height(12.dp))
//                   Text("Simulating fetch request (2.5s)...", color = Color.Gray, fontSize = 14.sp)
                }
            }

            is UiState.Success -> {
                HorizontalCardList(
                    itemList = currentState.items,
                    addToCart = { cartViewModel.addToCart(it) }
                )
            }

            is UiState.Error -> {
                Text(currentState.message, color = Color.Red, fontSize = 14.sp)
            }
        }

        Text(
            "Burgers", Modifier.padding(16.dp, 4.dp),
            fontSize = 18.sp, fontWeight = FontWeight.SemiBold
        )
        HorizontalCardList(
            itemList = fakeApi.getProductsByCategory("Burgers"),
            addToCart = { cartViewModel.addToCart(it) }
        )

        Text(
            "Pizza", Modifier.padding(16.dp, 4.dp),
            fontSize = 18.sp, fontWeight = FontWeight.SemiBold
        )
        HorizontalCardList(
            itemList = fakeApi.getProductsByCategory("Pizza"),
            addToCart = { cartViewModel.addToCart(it) }
        )

        Text(
            "Salads", Modifier.padding(16.dp, 4.dp),
            fontSize = 18.sp, fontWeight = FontWeight.SemiBold
        )
        HorizontalCardList(
            itemList =  fakeApi.getProductsByCategory("Salads"),
            addToCart = { cartViewModel.addToCart(it) })

        Text(
            "Drinks", Modifier.padding(16.dp, 4.dp),
            fontSize = 18.sp, fontWeight = FontWeight.SemiBold
        )
        HorizontalCardList(
            itemList =  fakeApi.getProductsByCategory("Drinks"),
            addToCart = { cartViewModel.addToCart(it) })


    }
}


@Composable
fun FoodItem(item: Product, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .width(185.dp)
            .height(225.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Column() {
                Image(
                    painter = painterResource(item.image),
                    contentDescription = "burger",
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .size(120.dp)
                )
                Column(
                    modifier = Modifier.padding(top = 20.dp, start = 11.dp)
                ) {
                    Text(item.title, Modifier, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("Wendy's Burger", Modifier, fontSize = 16.sp)
                }
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp),
//                        .background(Color.Red),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RatingData()
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
//            Image(
//                painter = painterResource(R.drawable.empty_heart),
//                contentDescription = null,
//                modifier = Modifier
//                    .align(Alignment.TopEnd) // Positions in top-right
//                    .padding(8.dp)
//            )
            IconButton(
                onClick = {
//                    Log.d("TAG", item.title )
                    cartViewModel.addToCart(
                        CartItem(
                            item.id, imageUrl = item.image.toString(),
                            name = item.title, price = item.price, quantity = 1
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Positions in top-right
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add to cart",
                    modifier = Modifier.size(32.dp), // Sets the internal icon size
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun RatingData() {
    Row(
        modifier = Modifier.padding(top = 15.dp, start = 11.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.filled_star),
            contentDescription = null,
            modifier = Modifier
        )
        Text("4.9", Modifier, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

data class SlidingContent(
    val text: String,
    val selected: Boolean = false,
)
