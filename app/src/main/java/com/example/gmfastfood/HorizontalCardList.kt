package com.example.gmfastfood

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gmfastfood.data.Product

@Composable
fun HorizontalCardList(itemList: List<Product>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp), // Padding for the entire row
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Space between cards
    ) {
        items(itemList) { item ->
            Card(
                modifier = Modifier
                    .width(160.dp)
                    .height(160.dp)
//                      .fillMaxWidth()
                ,
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
            ) {
                Column() {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(item.image),
                            contentDescription = "burger",
                                    contentScale = ContentScale.Crop, // Crops image to fill the width
                            modifier = Modifier
                                .height(130.dp)
                                .fillMaxWidth()
                        )

                    }
                    Text(text = item.title,  textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth() )
                }
            }
        }
    }
}