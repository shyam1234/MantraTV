package com.malviya.mantra.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

enum class FormFactor {
    Phone,
    Tablet,
    Tv
}

/**
 * Spacing and type sizes chosen so the chant screen stays inside the
 * visible area on a phone, a tablet, and a television.
 */
data class ScreenFit(
    val formFactor: FormFactor,
    val compact: Boolean,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val mantraFontSize: TextUnit,
    val mantraLineHeight: TextUnit,
    val flashFontSize: TextUnit,
    val logFontSize: TextUnit,
    val feedbackFontSize: TextUnit,
    val buttonFontSize: TextUnit,
    val autoChantFontSize: TextUnit,
    val watermarkFontSize: TextUnit,
    val itemSpacing: Dp,
    val buttonHorizontalPadding: Dp,
    val buttonVerticalPadding: Dp,
    val wordGap: Dp,
)

fun screenFitFor(widthDp: Int, heightDp: Int, isTelevision: Boolean): ScreenFit {
    val smallest = min(widthDp, heightDp)
    val formFactor = when {
        isTelevision -> FormFactor.Tv
        smallest >= 600 -> FormFactor.Tablet
        else -> FormFactor.Phone
    }
    // Landscape phones and short 720p panels need tighter type.
    val compact = heightDp < 480

    val horizontalPadding = when (formFactor) {
        // Android TV overscan safe area is 5% and at least 48dp.
        FormFactor.Tv -> (widthDp * 0.05f).coerceAtLeast(48f).dp
        FormFactor.Tablet -> 28.dp
        FormFactor.Phone -> if (widthDp < 360) 8.dp else 12.dp
    }
    val verticalPadding = when {
        FormFactor.Tv == formFactor -> (heightDp * 0.05f).coerceAtLeast(27f).dp
        compact -> 4.dp
        formFactor == FormFactor.Tablet -> 16.dp
        else -> 8.dp
    }

    val mantraFontSize = when (formFactor) {
        FormFactor.Phone -> if (compact) 16.sp else 20.sp
        FormFactor.Tablet -> 28.sp
        FormFactor.Tv -> if (compact) 26.sp else 32.sp
    }
    val mantraLineHeight = when (formFactor) {
        FormFactor.Phone -> if (compact) 22.sp else 28.sp
        FormFactor.Tablet -> 36.sp
        FormFactor.Tv -> if (compact) 32.sp else 42.sp
    }
    val flashFontSize = when (formFactor) {
        FormFactor.Phone -> 14.sp
        else -> 18.sp
    }
    val logFontSize = when (formFactor) {
        FormFactor.Phone -> if (compact) 13.sp else 16.sp
        else -> 20.sp
    }
    val feedbackFontSize = when (formFactor) {
        FormFactor.Phone -> if (compact) 12.sp else 14.sp
        else -> 18.sp
    }
    val buttonFontSize = when (formFactor) {
        FormFactor.Phone -> if (compact) 16.sp else 20.sp
        FormFactor.Tablet -> 26.sp
        FormFactor.Tv -> 28.sp
    }
    val autoChantFontSize = when (formFactor) {
        FormFactor.Phone -> if (compact) 12.sp else 14.sp
        FormFactor.Tablet -> 16.sp
        FormFactor.Tv -> 18.sp
    }
    val watermarkFontSize = when (formFactor) {
        FormFactor.Phone -> 11.sp
        FormFactor.Tablet -> 13.sp
        FormFactor.Tv -> 14.sp
    }
    val itemSpacing = when {
        compact -> 2.dp
        formFactor == FormFactor.Phone -> 4.dp
        else -> 8.dp
    }
    val buttonHorizontalPadding = when (formFactor) {
        FormFactor.Phone -> 8.dp
        FormFactor.Tablet -> 16.dp
        FormFactor.Tv -> 20.dp
    }
    val buttonVerticalPadding = when {
        compact -> 4.dp
        formFactor == FormFactor.Phone -> 8.dp
        formFactor == FormFactor.Tablet -> 10.dp
        else -> 12.dp
    }
    val wordGap = if (formFactor == FormFactor.Phone) 2.dp else 4.dp

    return ScreenFit(
        formFactor = formFactor,
        compact = compact,
        horizontalPadding = horizontalPadding,
        verticalPadding = verticalPadding,
        mantraFontSize = mantraFontSize,
        mantraLineHeight = mantraLineHeight,
        flashFontSize = flashFontSize,
        logFontSize = logFontSize,
        feedbackFontSize = feedbackFontSize,
        buttonFontSize = buttonFontSize,
        autoChantFontSize = autoChantFontSize,
        watermarkFontSize = watermarkFontSize,
        itemSpacing = itemSpacing,
        buttonHorizontalPadding = buttonHorizontalPadding,
        buttonVerticalPadding = buttonVerticalPadding,
        wordGap = wordGap,
    )
}

val LocalScreenFit = staticCompositionLocalOf {
    screenFitFor(widthDp = 360, heightDp = 800, isTelevision = false)
}

@Composable
fun rememberScreenFit(): ScreenFit {
    val configuration = LocalConfiguration.current
    val isTelevision =
        configuration.uiMode and Configuration.UI_MODE_TYPE_MASK ==
            Configuration.UI_MODE_TYPE_TELEVISION
    return screenFitFor(
        widthDp = configuration.screenWidthDp,
        heightDp = configuration.screenHeightDp,
        isTelevision = isTelevision
    )
}
