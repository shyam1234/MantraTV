package com.malviya.mantra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.malviya.mantra.ui.constants.AppConstants
import com.malviya.mantra.ui.theme.textColorMantra

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
@Composable
fun MantraHighlightedText(
    mantraText: String,
    highlightedWordIndex: Int,
    isAudioPlaying: Boolean
) {
    val words = mantraText.split(" ")
    val highlightedColor = Color(0xFFFFD700) // Gold color for highlighting
    val normalColor = textColorMantra
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppConstants.Dimensions.SPACING_MEDIUM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First line: हरे कृष्ण, हरे कृष्ण, कृष्ण कृष्ण, हरे हरे|
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0..7) {
                if (i < words.size) {
                    val isHighlighted = isAudioPlaying && i == highlightedWordIndex
                    val textColor = if (isHighlighted) highlightedColor else normalColor
                    val backgroundColor = if (isHighlighted) Color(0x33FFD700) else Color.Transparent
                    
                    Text(
                        text = words[i],
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = AppConstants.Typography.FONT_SIZE_XXLARGE,
                            lineHeight = AppConstants.Typography.LINE_HEIGHT_MANTRA
                        ),
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    
                    if (i < 7) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Second line: हरे राम, हरे राम, राम राम, हरे हरे||
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 8..15) {
                if (i < words.size) {
                    val isHighlighted = isAudioPlaying && i == highlightedWordIndex
                    val textColor = if (isHighlighted) highlightedColor else normalColor
                    val backgroundColor = if (isHighlighted) Color(0x33FFD700) else Color.Transparent
                    
                    Text(
                        text = words[i],
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = AppConstants.Typography.FONT_SIZE_XXLARGE,
                            lineHeight = AppConstants.Typography.LINE_HEIGHT_MANTRA
                        ),
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                color = backgroundColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    
                    if (i < 15) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
} 