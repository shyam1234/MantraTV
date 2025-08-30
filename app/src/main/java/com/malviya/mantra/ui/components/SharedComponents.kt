package com.malviya.mantra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.malviya.mantra.R
import com.malviya.mantra.ui.constants.AppConstants
import com.malviya.mantra.ui.screen.ChantLog
import com.malviya.mantra.ui.theme.colorButtonGray
import com.malviya.mantra.ui.theme.colorButtonGreen
import com.malviya.mantra.ui.theme.textColorButton
import com.malviya.mantra.ui.theme.textColorPowerBy
import com.malviya.mantra.ui.theme.textColorSuggestion
import com.malviya.mantra.ui.theme.textWhiteColorButton
import com.malviya.mantra.ui.viewmodel.ChantViewModel

/**
 * Main layout wrapper for mantra screens (counter and mala)
 *
 * @param name The mantra text to display
 * @param viewModel The ChantViewModel for state management
 * @param flashMessage The message to display in the flash banner
 * @param centerContent The main content to display in the center (counter or mala)
 * @param onIncrement Callback for increment button
 * @param onDecrement Callback for decrement button
 *
 * Provides a consistent layout structure for both counter and mala screens,
 * including mantra display, logs, buttons, and watermark.
 */
@Composable
fun MantraScreenLayout(
    name: String,
    viewModel: ChantViewModel,
    flashMessage: String,
    centerContent: @Composable () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val chantLogs by viewModel.chantLogs.collectAsState()
    val chantFeedback by viewModel.chantFeedback.collectAsState()
    val context = LocalContext.current
    val poweredBy = stringResource(id = R.string.powered_by)
    val buildNumber = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val sampurnamalaFormat = context.getString(R.string.sampurnamala)
    val suggestion = getChantFeedback(chantFeedback)

    DynamicBackground {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
           val (flashRef, contentRef, watermarkRef) = createRefs()

            // Top Flash Message
            FlashMessage(
                flashMessage,
                modifier = Modifier
                    .constrainAs(flashRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = AppConstants.Dimensions.SPACING_TINY)
            )

            // Scrollable Center Content
            Column(
                modifier = Modifier
                    .constrainAs(contentRef) {
                        top.linkTo(flashRef.bottom)
                        bottom.linkTo(watermarkRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppConstants.Dimensions.SPACING_TINY,
                        end = AppConstants.Dimensions.SPACING_TINY,
                        top = AppConstants.Dimensions.SPACING_TINY,
                        bottom = AppConstants.Dimensions.SPACING_TINY
                    )
            ) {
                // Use highlighted text when auto-chant is active, otherwise use regular mantra render
                val isAutoChanting by viewModel.isAutoChanting.collectAsState()
                val isAudioPlaying by viewModel.isAudioPlaying.collectAsState()
                val currentWordIndex by viewModel.currentWordIndex.collectAsState()
                
                if (isAutoChanting && isAudioPlaying) {
                    MantraHighlightedText(
                        mantraText = viewModel.getMantraText(),
                        highlightedWordIndex = currentWordIndex,
                        isAudioPlaying = isAudioPlaying
                    )
                } else {
                    MantraRender(name)
                }

                MantraLogsSection(
                    chantLogs = chantLogs,
                    sampurnamalaFormat = sampurnamalaFormat,
                    suggestion = suggestion,
                    viewModel = viewModel
                )

                centerContent()

                MantraButtonsSection(
                    viewModel = viewModel,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement
                )
            }

            // Bottom Watermark
            Text(
                text = "$poweredBy ${AppConstants.UI.WATERMARK_SEPARATOR} ${AppConstants.UI.WATERMARK_PREFIX}$buildNumber",
                color = textColorPowerBy,
                fontSize = AppConstants.Typography.FONT_SIZE_TINY,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .constrainAs(watermarkRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = AppConstants.Dimensions.SPACING_TINY)
            )
        }
    }
}


/**
 * Displays mantra logs and feedback section
 *
 * @param chantLogs List of completed mala rounds with timing
 * @param sampurnamalaFormat Formatted string for displaying mala completion info
 * @param suggestion Current chant speed feedback message
 * @param viewModel ChantViewModel for time conversion utilities
 *
 * Shows the last completed mala information and current chanting feedback.
 * Displays empty space if no malas have been completed yet.
 */
@Composable
private fun MantraLogsSection(
    chantLogs: List<ChantLog>,
    sampurnamalaFormat: String,
    suggestion: String,
    viewModel: ChantViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppConstants.Dimensions.SPACING_MEDIUM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (chantLogs.isNotEmpty()) {
            val totalTime = viewModel.convertMillisToReadableTime(chantLogs.last().totalTime)
            Text(
                text = sampurnamalaFormat.format(
                    chantLogs.last().malaNumber,
                    totalTime
                ),
                Modifier.padding(AppConstants.Dimensions.SPACING_SMALL),
                fontSize = AppConstants.Typography.FONT_SIZE_LARGE,
                color = textColorSuggestion,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = " ",
                Modifier.padding(AppConstants.Dimensions.SPACING_SMALL),
                fontSize = AppConstants.Typography.FONT_SIZE_LARGE,
                color = textColorSuggestion,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = suggestion,
            fontSize = AppConstants.Typography.FONT_SIZE_MEDIUM,
            modifier = Modifier.padding(AppConstants.Dimensions.SPACING_XXLARGE),
            color = textColorSuggestion,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Displays the control buttons for mantra chanting
 *
 * @param viewModel ChantViewModel for state management
 * @param onIncrement Callback for increment button press
 * @param onDecrement Callback for decrement button press
 *
 * Shows three buttons: decrement (-1), auto-chant toggle, and increment (+1).
 * Button colors change based on auto-chant state to provide visual feedback.
 */
@Composable
private fun MantraButtonsSection(
    viewModel: ChantViewModel,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val decrementText = stringResource(id = R.string.decrement_button)
    val incrementText = stringResource(id = R.string.increment_button)
    val autoChantOnText = stringResource(id = R.string.auto_chant_on)
    val autoChantOffText = stringResource(id = R.string.auto_chant_off)
    val isAutoChanting by viewModel.isAutoChanting.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppConstants.Dimensions.SPACING_XLARGE),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onDecrement,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isAutoChanting) colorButtonGreen else colorButtonGray
            ),
        ) {
            Text(
                text = decrementText,
                fontSize = AppConstants.Typography.FONT_SIZE_XXLARGE,
                fontWeight = FontWeight.Bold,
                color = if (!isAutoChanting) textWhiteColorButton else textColorButton,
            )
        }

        // Auto Chant Toggle Button
        Button(
            onClick = { viewModel.toggleAutoChant() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAutoChanting) colorButtonGreen else colorButtonGray
            )
        ) {
            Text(
                text = if (isAutoChanting) autoChantOnText else autoChantOffText,
                fontSize = AppConstants.Typography.FONT_SIZE_LARGE,
                color = if (isAutoChanting) textWhiteColorButton else textColorButton,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onIncrement,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isAutoChanting) colorButtonGreen else colorButtonGray
            ),
        ) {
            Text(
                text = incrementText,
                fontSize = AppConstants.Typography.FONT_SIZE_XXLARGE,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/**
 * Converts chant feedback state to localized string
 *
 * @param chantFeedback The current chant speed feedback state
 * @return Localized string message for the feedback state
 *
 * Maps the ChantFeedback enum to appropriate localized string resources
 * for displaying user feedback about chanting speed.
 */
@Composable
private fun getChantFeedback(chantFeedback: ChantViewModel.ChantFeedback): String {
    return when (chantFeedback) {
        is ChantViewModel.ChantFeedback.Begin -> stringResource(id = R.string.chant_feedback_begin)
        is ChantViewModel.ChantFeedback.VeryFast -> stringResource(id = R.string.chant_feedback_very_fast)
        is ChantViewModel.ChantFeedback.Fast -> stringResource(id = R.string.chant_feedback_fast)
        is ChantViewModel.ChantFeedback.Good -> stringResource(id = R.string.chant_feedback_good)
        is ChantViewModel.ChantFeedback.SlowThanUsual -> stringResource(id = R.string.chant_feedback_slow_than_usual)
        is ChantViewModel.ChantFeedback.Slow -> stringResource(id = R.string.chant_feedback_slow)
        is ChantViewModel.ChantFeedback.VerySlow -> stringResource(id = R.string.chant_feedback_very_slow)
    }
} 