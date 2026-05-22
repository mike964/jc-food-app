package com.example.gmfastfood

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.vm.CartItem

@Composable
fun HorizontalCardList(itemList: List<Product>, addToCart: (CartItem) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp), // Padding for the entire row
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Space between cards
    ) {
        items(itemList) { item ->
            Column(
                modifier = Modifier
                    .width(128.dp)
            ) {
                Card(
                    modifier = Modifier
                        .width(128.dp)
                        .height(128.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Column() {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(item.image),
                                contentDescription = "burger",
                                contentScale = ContentScale.Crop, // Crops image to fill the width
                                modifier = Modifier
//                                    .height(120.dp)
                                    .fillMaxSize()
                            )
                            IconButton(
                                onClick = {
//                    Log.d("TAG", item.title )
                                    addToCart(
                                        CartItem(
                                            item.id,
                                            imageUrl = item.image.toString(),
                                            name = item.title,
                                            price = item.price,
                                            quantity = 1
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd) // Positions in top-right
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddCircle,
                                    contentDescription = "Add to cart",
                                    modifier = Modifier
                                        .size(32.dp) // Sets the internal icon size
                                        .background(color = Color.White, shape = CircleShape),
                                    tint = Color.Red,
                                )
                            }
                        }
                    }
                }
                Column(
                    Modifier
                        .height(50.dp)
                        .padding(2.dp)
                    //  .background(Color.Yellow)
                ) {
                    Text(
                        text = item.title, textAlign = TextAlign.Center, fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = item.price.toInt().toString(), textAlign = TextAlign.Center, fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

        }
    }
}