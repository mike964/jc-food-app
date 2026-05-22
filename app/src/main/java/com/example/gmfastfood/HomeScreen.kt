package com.example.gmfastfood

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
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.data.filterProductsByCategory
import com.example.gmfastfood.data.products
import com.example.gmfastfood.navigation.SharedViewModel
import com.example.gmfastfood.vm.CartItem
import com.example.gmfastfood.vm.CartViewModel

@Composable
fun HomeScreen(viewModel: SharedViewModel, cartViewModel: CartViewModel) {
    val text by viewModel.sharedText.collectAsState()
    val scrollState =  rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text("Food Go", Modifier, fontSize = 45.sp, fontWeight = FontWeight.SemiBold)
        Text(
            "Order your favourite food!", Modifier, fontSize = 15.sp,
            color = Color(0xff6A6A6A),
            fontWeight = FontWeight.Medium
        )

        SearchBox(textValue = text, onValueChange = { viewModel.updateText(it) })
        HorizontalSlider()

        HorizontalList(itemsList = listOf("Burgers", "Pizza", "Sushi", "Drinks", "Desserts", "Salads", "Pasta", "Snacks", "Soup"))

//        HorizontalCardList(itemList = listOf("Burgers", "Pizza", "Sushi", "Drinks", "Desserts", "Salads", "Pasta", "Snacks", "Soup"))
        Spacer(modifier = Modifier.height(10.dp))
        Text("Popular", Modifier, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
//        HorizontalCardList(itemList = listOf("Burgers", "Pizza", "Sushi", "Drinks", "Desserts", "Salads", "Pasta", "Snacks", "Soup"))
        Text("Special Offers", Modifier, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        HorizontalCardList(itemList = products)
        Spacer(modifier = Modifier.height(10.dp))

        Text("Drinks", Modifier, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalCardList(itemList = filterProductsByCategory("Drinks"))



//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            horizontalArrangement = Arrangement.spacedBy(4.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.fillMaxSize()
//            ) {
//            items(products) { item ->
//                FoodItem(item, cartViewModel)
//            }
//        }
    }
}


@Composable
fun FoodItem(item: Product, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier.padding(8.dp, 4.dp)
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
                    cartViewModel.addToCart(CartItem(item.id,   imageUrl = item.image.toString(),
                        name=item.title, price=item.price, quantity = 1) )
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

@Preview
@Composable
fun HomeScreenPreview() {
//    FoodItem(products[0])
}