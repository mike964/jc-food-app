package com.example.gmfastfood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun JumpToSection() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var targetA by remember { mutableFloatStateOf(0f) }
    var targetB by remember { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Button(onClick = {
            scope.launch { scrollState.animateScrollTo(targetA.toInt()) }
        }) {
            Text("Jump to Target A")
        }
        Button(onClick = {
            scope.launch { scrollState.animateScrollTo(targetB.toInt()) }
        }) {
            Text("Jump to Target B")
        }

        Spacer(Modifier.height(900.dp))

        Text(
            "Target Section 1",
            modifier = Modifier.onGloballyPositioned { coordinates ->
                targetA = coordinates.positionInRoot().y
            }.background(Color.Red)
        )
        Spacer(Modifier.height(900.dp))

        Text(
            "Target Section 2",
            modifier = Modifier.onGloballyPositioned { coordinates ->
                targetB = coordinates.positionInRoot().y
            }.background(Color.Blue)
        )
        Spacer(Modifier.height(900.dp))
    }
}
