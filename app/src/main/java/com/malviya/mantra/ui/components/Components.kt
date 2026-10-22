package com.malviya.mantra.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.constants.AppConstants
import com.malviya.mantra.ui.theme.highlightedTextBGColorMantra
import com.malviya.mantra.ui.theme.highlightedTextColorMantra
import com.malviya.mantra.ui.theme.textColorMantra
import kotlin.math.cos
import kotlin.math.sin




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

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val available = minOf(maxWidth, maxHeight)
        if (available > 1.dp) {
            val outer = available * 0.92f
            val beadRadius = (outer * 0.02f).coerceIn(2.dp, 7.dp)
            Box(Modifier.size(outer), contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(beadRadius)
                ) {
                    drawCircle(
                        color = color,
                        radius = size.minDimension / 2f,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }

                Text(
                    text = "$count",
                    fontSize = (outer.value * 0.22f).coerceIn(18f, 64f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val circleRadius = beadRadius.toPx()
                    val orbit = size.minDimension / 2f - circleRadius
                    for (i in 0 until count.coerceAtMost(AppConstants.ONE_MALA_ROUND_COUNT)) {
                        val angle = (i * 2 * Math.PI / AppConstants.ONE_MALA_ROUND_COUNT).toFloat()
                        val x = orbit * cos(angle) + size.width / 2f
                        val y = orbit * sin(angle) + size.height / 2f
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
fun FlashMessage(message: String, modifier: Modifier) {
    Box(
        modifier = modifier
    ){
        Text(
            message,
            maxLines = AppConstants.UI.MAX_LINES_FLASH_MESSAGE,
            softWrap = false,
            color = Color.White,
            fontSize = LocalScreenFit.current.flashFontSize,
            fontWeight = FontWeight.Bold
        )
    }
}


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
    val fit = LocalScreenFit.current
    Text(
        text = name,
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = fit.mantraFontSize,
            lineHeight = fit.mantraLineHeight
        ),
        textAlign = TextAlign.Center,
        color = textColorMantra,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(horizontal = fit.itemSpacing, vertical = fit.itemSpacing)
            .fillMaxWidth()
    )
}


/**
 * Component for displaying mantra text with word highlighting
 *
 * @param mantraText The complete mantra text to display
 * @param highlightedWordIndex Index of the currently highlighted word (-1 for no highlight)
 * @param isAudioPlaying Whether audio is currently playing
 *
 * Displays the mantra text with individual word highlighting that can be
 * synchronized with audio playback for visual feedback.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MantraHighlightedText(
    mantraText: String,
    highlightedWordIndex: Int,
    isAudioPlaying: Boolean
) {
    val words = mantraText.split(" ")
    val fit = LocalScreenFit.current
    val highlightedColor = highlightedTextBGColorMantra
    val normalColor = textColorMantra

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = fit.itemSpacing, vertical = fit.itemSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MantraWordRow(
            words = words,
            startIndex = 0,
            endIndex = 7,
            highlightedWordIndex = highlightedWordIndex,
            isAudioPlaying = isAudioPlaying,
            highlightedColor = highlightedColor,
            normalColor = normalColor,
            fit = fit
        )
        MantraWordRow(
            words = words,
            startIndex = 8,
            endIndex = 15,
            highlightedWordIndex = highlightedWordIndex,
            isAudioPlaying = isAudioPlaying,
            highlightedColor = highlightedColor,
            normalColor = normalColor,
            fit = fit
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MantraWordRow(
    words: List<String>,
    startIndex: Int,
    endIndex: Int,
    highlightedWordIndex: Int,
    isAudioPlaying: Boolean,
    highlightedColor: Color,
    normalColor: Color,
    fit: ScreenFit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in startIndex..endIndex) {
            if (i < words.size) {
                val isHighlighted = isAudioPlaying && i == highlightedWordIndex
                Text(
                    text = words[i],
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = fit.mantraFontSize,
                        lineHeight = fit.mantraLineHeight
                    ),
                    textAlign = TextAlign.Center,
                    color = if (isHighlighted) highlightedColor else normalColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = if (isHighlighted) highlightedTextColorMantra else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = fit.wordGap, vertical = 0.dp)
                )
            }
        }
    }
}


