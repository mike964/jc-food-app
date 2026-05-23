package com.example.gmfastfood

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.navigation.SharedViewModel

@Composable
fun SearchBox( textValue : String ,  onValueChange: (String) -> Unit) {
//    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xfff3f4f6))
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Filled.Search, contentDescription = null)
            BasicTextField(
                value = textValue, // Current value shown
//                onValueChange = { newText -> text = newText }, // Updates state on keystroke
                onValueChange = { onValueChange(it) }, // Updates state on keystroke
                //placeholder = { Text("Search something...") } // Hint text when empty
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 20.sp)
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 15.dp)
                .size(48.dp)
                .background(Color(0xffEF2A39), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_basket),
                contentDescription = null,
            )
        }
    }
}