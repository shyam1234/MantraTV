package com.malviya.mantra.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.constants.AppConstants

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = AppConstants.Typography.FONT_SIZE_MEDIUM,
        lineHeight = AppConstants.Typography.LINE_HEIGHT_DEFAULT,
        letterSpacing = AppConstants.Typography.LETTER_SPACING_DEFAULT
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = AppConstants.Typography.FONT_SIZE_LARGE,
        lineHeight = AppConstants.Typography.LINE_HEIGHT_TITLE,
        letterSpacing = AppConstants.Typography.LETTER_SPACING_TITLE
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = AppConstants.Typography.FONT_SIZE_TINY,
        lineHeight = AppConstants.Typography.LINE_HEIGHT_CAPTION,
        letterSpacing = AppConstants.Typography.LETTER_SPACING_DEFAULT
    )
    */
)