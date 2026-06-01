package com.example.gmfastfood.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ProductCard(
    name: String,
    price: String,
    imageUrl: String,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Product Image
//            AsyncImage(
//                model = imageUrl,
//                contentDescription = name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp),
//                contentScale = ContentScale.Crop
//            )

            Column(modifier = Modifier.padding(12.dp)) {
                // Product Name
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Product Price
                Text(
                    text = price,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Action Button
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Add to Cart")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview(){
    ProductCard(
        name = "Product Name",
        price = "$19.99",
        imageUrl = "",
        onAddToCart = {}
    )
}
