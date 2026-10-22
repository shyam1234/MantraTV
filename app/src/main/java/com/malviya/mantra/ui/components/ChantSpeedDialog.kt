package com.malviya.mantra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.malviya.mantra.R
import com.malviya.mantra.ui.settings.ChantSpeeds
import com.malviya.mantra.ui.theme.colorButtonGray
import com.malviya.mantra.ui.theme.colorButtonGreen
import com.malviya.mantra.ui.theme.textColorMantra
import com.malviya.mantra.ui.theme.textColorSuggestion
import com.malviya.mantra.ui.theme.textWhiteColorButton
import com.malviya.mantra.ui.viewmodel.ChantViewModel

@Composable
fun ChantSpeedDialog(
    viewModel: ChantViewModel,
    onDismiss: () -> Unit
) {
    val speed by viewModel.chantSpeed.collectAsState()
    val snapped = ChantSpeeds.nearest(speed)
    val fit = LocalScreenFit.current
    val speedName = when (snapped) {
        0.5f -> stringResource(R.string.chant_speed_very_slow)
        0.75f -> stringResource(R.string.chant_speed_slow)
        1.0f -> stringResource(R.string.chant_speed_normal)
        1.25f -> stringResource(R.string.chant_speed_fast)
        1.5f -> stringResource(R.string.chant_speed_very_fast)
        1.75f -> stringResource(R.string.chant_speed_faster)
        else -> stringResource(R.string.chant_speed_fastest)
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .widthIn(min = 280.dp, max = 520.dp)
                .background(color = Color(0xFF1C140C), shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.chant_speed_title),
                color = textColorMantra,
                fontSize = fit.logFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.chant_speed_value, speedName, ChantSpeeds.format(snapped)),
                color = textWhiteColorButton,
                fontSize = fit.mantraFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.setChantSpeed(ChantSpeeds.slower(snapped)) },
                    enabled = snapped > ChantSpeeds.min,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorButtonGreen,
                        contentColor = textWhiteColorButton,
                        disabledContainerColor = colorButtonGray,
                        disabledContentColor = textWhiteColorButton
                    )
                ) {
                    Text(
                        text = stringResource(R.string.chant_speed_slower),
                        fontSize = fit.feedbackFontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
                Button(
                    onClick = { viewModel.setChantSpeed(ChantSpeeds.faster(snapped)) },
                    enabled = snapped < ChantSpeeds.max,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorButtonGreen,
                        contentColor = textWhiteColorButton,
                        disabledContainerColor = colorButtonGray,
                        disabledContentColor = textWhiteColorButton
                    )
                ) {
                    Text(
                        text = stringResource(R.string.chant_speed_faster_button),
                        fontSize = fit.feedbackFontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.chant_speed_saved),
                color = textColorSuggestion,
                fontSize = fit.feedbackFontSize,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorButtonGray,
                    contentColor = textWhiteColorButton
                )
            ) {
                Text(
                    text = stringResource(R.string.chant_speed_close),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
