package com.malviya.mantra.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
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
import com.malviya.mantra.ui.constants.AppConstants
import com.malviya.mantra.ui.theme.textColorMantra
import kotlin.math.cos
import kotlin.math.sin


/**
 * Renders the main mantra text with proper styling
 * 
 * @param name The mantra text to display
 * 
 * Displays the mantra in large, bold text with proper spacing and alignment.
 * Uses the theme's mantra color for consistent branding.
 */
@Composable
fun MantraRender(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = AppConstants.Typography.FONT_SIZE_XXXLARGE, 
            lineHeight = AppConstants.Typography.LINE_HEIGHT_MANTRA
        ),
        textAlign = TextAlign.Center,
        color = textColorMantra,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(AppConstants.Dimensions.SPACING_MEDIUM)
            .fillMaxWidth(AppConstants.Layout.FILL_MAX_WIDTH)
    )
}

/**
 * Simple circular counter display
 * 
 * @param number The number to display in the center
 * @param color The background color of the circle
 * 
 * Displays a number in a circular container with the specified background color.
 * Used for basic counter visualization.
 */
@Composable
fun GrayCircleWithNumber(number: Int, color: Color) {
    Box(
        modifier = Modifier
            .padding(AppConstants.Dimensions.SPACING_LARGE)
            .size(AppConstants.Dimensions.CIRCLE_MAIN_SIZE)
            .background(color, shape = CircleShape), 
        contentAlignment = Alignment.Center
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = AppConstants.Typography.FONT_SIZE_COUNTER,
            text = number.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


/**
 * Advanced circular counter with bead indicators
 * 
 * @param count The current bead count (0-108)
 * @param color The color for the current bead and main circle
 * 
 * Displays a main circle with the count in the center, surrounded by small circles
 * representing each bead in the mala. Completed beads show their respective colors,
 * providing visual feedback of progress through the mala round.
 */
@Composable
fun GrayCircleWithNumber2(count: Int, color: Color) {
    // State to keep track of the colors for each bead circle
    var circleColors by remember { mutableStateOf(MutableList(AppConstants.ONE_MALA_ROUND_COUNT) { Color.Transparent }) }
    
    // Reset bead colors when count reaches 0 (new mala round)
    LaunchedEffect(count) {
        if (count == 0) {
            circleColors = MutableList(AppConstants.ONE_MALA_ROUND_COUNT) { Color.Transparent }
        }
    }

    // Update the colors list only when count increases and add the new color
    if (count > circleColors.count { it != Color.Transparent }) {
        circleColors[count - 1] = color
    }

    Box(contentAlignment = Alignment.Center) {
        // Main circle with the count in center
        Canvas(modifier = Modifier.size(AppConstants.Dimensions.CIRCLE_MAIN_SIZE)) {
            drawCircle(
                color = color,
                radius = size.minDimension * AppConstants.Layout.CIRCLE_RADIUS_FACTOR,
                center = Offset(size.width * AppConstants.Layout.CENTER_ALIGNMENT, size.height * AppConstants.Layout.CENTER_ALIGNMENT)
            )
        }

        // Display current count in center
        Text(
            text = "$count",
            fontSize = AppConstants.Typography.FONT_SIZE_COUNTER,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

        // Small colored circles forming a border around the main circle
        Canvas(modifier = Modifier.size(AppConstants.Dimensions.CIRCLE_BORDER_SIZE)) {
            val circleRadius = AppConstants.Dimensions.SMALL_CIRCLE_RADIUS.toPx()
            val bigCircleRadius = size.minDimension * AppConstants.Layout.CIRCLE_RADIUS_FACTOR - circleRadius

            // Draw bead indicator circles based on the count
            for (i in 0 until count.coerceAtMost(AppConstants.ONE_MALA_ROUND_COUNT)) {
                if (count > 0) {
                    val angle = (i * 2 * Math.PI / AppConstants.ONE_MALA_ROUND_COUNT).toFloat()
                    val x = bigCircleRadius * cos(angle) + size.width * AppConstants.Layout.CENTER_ALIGNMENT
                    val y = bigCircleRadius * sin(angle) + size.height * AppConstants.Layout.CENTER_ALIGNMENT
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

/**
 * Displays a scrolling flash message at the top of the screen
 * 
 * @param message The message text to display
 * 
 * Shows important announcements or notifications in a marquee-style scrolling text.
 * Uses infinite scrolling animation with configurable speed and styling.
 */
@Composable
fun FlashMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(
                iterations = AppConstants.MARQUEE_ITERATIONS, 
                animationMode = MarqueeAnimationMode.Immediately, 
                velocity = AppConstants.MARQUEE_VELOCITY.dp
            )
            .padding(AppConstants.Dimensions.SPACING_MEDIUM),
        contentAlignment = Alignment.TopCenter
    ){
        Text(
            message,
            maxLines = AppConstants.UI.MAX_LINES_FLASH_MESSAGE,
            color = Color.White,
            fontSize = AppConstants.Typography.FONT_SIZE_SMALL,
            fontWeight = FontWeight.Bold
        )
    }
}


