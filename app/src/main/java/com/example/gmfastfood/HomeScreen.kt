package com.example.gmfastfood

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.data.products
import com.example.gmfastfood.navigation.SharedViewModel

@Composable
fun HomeScreen(viewModel: SharedViewModel) {
    val text by viewModel.sharedText.collectAsState()

    Column(
        modifier= Modifier.fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ){
        Text("Food Go", Modifier, fontSize = 45.sp, fontWeight = FontWeight.SemiBold, )
        Text("Order your favourite food!", Modifier, fontSize = 15.sp,
            color = Color(0xff6A6A6A),
            fontWeight = FontWeight.Medium)

        TextField(
            value = text,
            onValueChange = { viewModel.updateText(it) },
            label = { Text("Shared Data") }
        )

        SearchBox( textValue = text, onValueChange = { viewModel.updateText(it) })
        HorizontalSliderItems()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(vertical = 12.dp),

            ) {
            items(products) { item ->
                FoodItem(item)
            }
        }

    }
}

@Composable
fun HorizontalSliderItems(){
    val texts = listOf<SlidingContent>(
        SlidingContent("All", true),
        SlidingContent( "Combos",false),
        SlidingContent("Sliders", false),
        SlidingContent("Classics",false)
    )
    Row(
        modifier =Modifier
            .padding(horizontal = 11.dp, vertical = 20.dp )
            .fillMaxWidth().
            wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        texts.forEach {
            Box(
                modifier = Modifier.height(40.dp).wrapContentWidth()
                    .background( if(it.selected)Color(0xffEF2A39) else Color(0xffF3F4F6), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(it.text, Modifier, color = if(it.selected) Color.White else Color(0xff6a6a6a), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


@Composable
fun FoodItem(item: Product){
    Card(
        modifier = Modifier.width(185.dp).height(225.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ){
        Image(
            painter = painterResource(item.image),
            contentDescription = "burger",
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .size(120.dp)
        )
        Column(
            modifier = Modifier.padding(top = 20.dp, start = 11.dp)
        ){
            Text(item.title, Modifier, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("Wendy's Burger", Modifier, fontSize = 16.sp)
        }
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            RatingData()
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.empty_heart),
                contentDescription = null,
                modifier = Modifier.padding(top = 5.dp,end = 5.dp)
            )
        }

    }
}

@Composable
fun RatingData(){
    Row(
        modifier =Modifier.padding(top = 15.dp, start = 11.dp)
    ){
        Image(
            painter = painterResource(R.drawable.filled_star),
            contentDescription = null,
            modifier = Modifier
        )
        Text("4.9", Modifier, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
data class SlidingContent(
    val text : String,
    val selected : Boolean = false,
)

