package com.example.gmfastfood.checkout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// 1. Define the possible states
enum class SubmitState {
    Idle, Loading, Success
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSubmitButton(
    state: SubmitState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 2. Animate background color based on state
    val backgroundColor by animateColorAsState(
        targetValue = when (state) {
            SubmitState.Idle -> Color(0xFF6200EE) // Purple
            SubmitState.Loading -> Color(0xFF6200EE)
            SubmitState.Success -> Color(0xFF4BB543) // Green
        },
        animationSpec = tween(durationMillis = 300),
        label = "ButtonColor"
    )

    // 3. Dynamic width changes depending on state
    val buttonWidth = when (state) {
        SubmitState.Idle -> 200.dp
        SubmitState.Loading, SubmitState.Success -> 56.dp // Collapse into a circle
    }

    Button(
        onClick = { if (state == SubmitState.Idle) onClick() },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = modifier
            .height(56.dp)
            .width(buttonWidth), // Standard material height
        // Optional: Add shape morphing if desired, though standard Button handles width well
    ) {
        // 4. Animate the content inside the button
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(animationSpec = tween(200)) with fadeOut(animationSpec = tween(200)) using
                        SizeTransform { _, _ -> tween(300) }
            },
            label = "ButtonContent"
        ) { targetState ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (targetState) {
                    SubmitState.Idle -> {
                        Text(text = "Submit", color = Color.White)
                    }
                    SubmitState.Loading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    SubmitState.Success -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Success",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}