package com.malviya.mantra.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val context = LocalContext.current
    val poweredBy = stringResource(id = R.string.powered_by)
    val buildNumber = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val sampurnamalaFormat = context.getString(R.string.sampurnamala)
    val chantFeedback by viewModel.chantFeedback.collectAsState()
    val suggestion = getChantFeedback(chantFeedback)

    DynamicBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                FlashMessage(flashMessage)
            }
            
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                MantraRender(name)

                // Mantra logs
                MantraLogsSection(chantLogs, sampurnamalaFormat, suggestion, viewModel)

                // Center content (counter circle or mala visualization)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = AppConstants.Dimensions.SPACING_XLARGE)
                ) {
                    centerContent()
                }

                // Mantra buttons
                MantraButtonsSection(
                    viewModel = viewModel,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement
                )
            }

            // Watermark text at the bottom center
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = AppConstants.Dimensions.SPACING_TINY)
            ) {
                Text(
                    text = "$poweredBy ${AppConstants.UI.WATERMARK_SEPARATOR} ${AppConstants.UI.WATERMARK_PREFIX}$buildNumber",
                    color = textColorPowerBy,
                    fontSize = AppConstants.Typography.FONT_SIZE_TINY,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

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