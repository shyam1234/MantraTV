package com.malviya.mantra.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.ONE_MALA_ROUND_COUNT
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MantraRender(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp, lineHeight = 60.sp),
        textAlign = TextAlign.Center,
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(1f)
    )
}

@Composable
fun GrayCircleWithNumber(number: Int, color: Color) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .size(140.dp) // Adjust size as needed
            .background(color, shape = CircleShape), contentAlignment = Alignment.Center

    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp,
            text = number.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge // Adjust text style as needed
        )
    }
}


@Composable
fun GrayCircleWithNumber2(count: Int, color: Color) {
    // State to keep track of the colors for each circle
    var circleColors by remember { mutableStateOf(MutableList(ONE_MALA_ROUND_COUNT) { Color.Transparent }) }
    // Check if count is 0 and reset circleColors
    LaunchedEffect(count) {
        if (count == 0) {
            circleColors = MutableList(ONE_MALA_ROUND_COUNT) { Color.Transparent }
        }
    }

    // Update the colors list only when count increases and add the new color
    if (count > circleColors.count { it != Color.Transparent }) {
        circleColors[count - 1] = color
    }

    Box(contentAlignment = Alignment.Center) {
        // Main circle with the count
        Canvas(modifier = Modifier.size(140.dp)) {
            drawCircle(
                color = color,
                radius = size.minDimension / 2,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

        Text(
            text = "$count",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

        // Small colored circles forming a border around the main circle
        Canvas(modifier = Modifier.size(150.dp)) {
            val circleRadius = 3f  // Radius of each small circle
            val bigCircleRadius =
                size.minDimension / 2 - circleRadius  // Adjusted radius for positioning

            // Draw circles based on the count
            for (i in 0 until count.coerceAtMost(ONE_MALA_ROUND_COUNT)) {
                if (count > 0) {
                    val angle = (i * 2 * Math.PI / ONE_MALA_ROUND_COUNT).toFloat()
                    val x = bigCircleRadius * cos(angle) + size.width / 2
                    val y = bigCircleRadius * sin(angle) + size.height / 2
                    drawCircle(
                        color = circleColors[i],
                        radius = circleRadius,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}


