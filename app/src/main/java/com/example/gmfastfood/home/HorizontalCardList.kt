package com.example.gmfastfood.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.R
import com.example.gmfastfood.data.CartItem
import com.example.gmfastfood.data.Product

@Composable
fun HorizontalCardList(
    itemList: List<Product>,
    addToCart: (CartItem) -> Unit,
    onUpdateQuantity: (Int, Int) -> Unit,
    cartItems: List<CartItem>,
) {
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
                val itemExistingInCart: CartItem? = cartItems.find { it.id == item.id }
                val itemExistsInCart: Boolean = cartItems.any { it.id == item.id }

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
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        if (itemExistsInCart) Color(0x55D8A90D)
                                        else Color(0x00D3B114)
                                    )
                                    .fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    Modifier.weight(2f)
                                    //   .background(Color.Blue)
                                ) {
                                    if (itemExistsInCart) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = {
                                                    onUpdateQuantity(
                                                        item.id,
                                                        itemExistingInCart?.quantity?.minus(1) ?: 0
                                                    )
                                                },
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.ic_remove),
                                                    contentDescription = "Remove from cart",
                                                    modifier = Modifier
                                                        .size(32.dp) // Sets the internal icon size
                                                        .background(
                                                            color = Color.White,
                                                            shape = CircleShape
                                                        ),
                                                    tint = Color.Red,
                                                )
                                            }
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(Color.White, shape = CircleShape)
                                                    .padding(4.dp) // Space between text and circle edge
                                                // Ensures width equals height
                                            ) {
                                                Text(
                                                    text = itemExistingInCart?.quantity.toString(),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp,
                                                    color = Color.Red,
//                                            modifier = Modifier.background(Color.White)
                                                )
                                            }
                                        }
                                    }
                                }
                                Column(
                                    Modifier.weight(1f)
                                    //  .background(Color.Green)
                                ) {
                                    IconButton(
                                        onClick = {
                                            addToCart(
                                                CartItem(
                                                    item.id,
                                                    imageUrl = item.image.toString(),
                                                    title = item.title,
                                                    price = item.price,
                                                    quantity = 1
                                                )
                                            )
                                        },
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.AddCircle,
                                            contentDescription = "Add to cart",
                                            modifier = Modifier
                                                .size(32.dp) // Sets the internal icon size
                                                .background(
                                                    color = Color.White,
                                                    shape = CircleShape
                                                ),
                                            tint = Color.Red,
                                        )
                                    }
                                }
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
                        text = item.price.toInt().toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

        }
    }
}