package com.malviya.mantra.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.ChantViewModel
import java.util.Locale
import com.malviya.mantra.R

// Data class to hold mala number and time consumed
data class ChantLog(val malaNumber: Int,
                    val timeConsumed: Long,
                    val totalTime : Long)


@Composable
fun GreetingScreen(name : String, viewModel: ChantViewModel) {
    val count by viewModel.count.collectAsState()
    val color by viewModel.color.collectAsState()
    val chantLogs by viewModel.chantLogs.collectAsState()
    val context = LocalContext.current
    val poweredBy = stringResource(id = R.string.powered_by)
    val sampurnamalaFormat = context.getString(R.string.sampurnamala)
    // Map feedback state to localized strings
    val chantFeedback by viewModel.chantFeedback.collectAsState()
    val suggestion = getChantFeedback(chantFeedback)

    val oneBeadTimeForRendering by viewModel.oneBeadTimeForRendering.collectAsState()
    val malaNumber by viewModel.malaNumber.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()), // Enables vertical scrolling
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MantraRender(name)

            Spacer(modifier = Modifier.weight(1f)) // Takes up remaining space

            if (chantLogs.isNotEmpty()) {
                val totalTime = viewModel.convertMillisToReadableTime(chantLogs.last().totalTime)
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp),
                    text = sampurnamalaFormat.format(chantLogs.last().malaNumber, totalTime),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }else{
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp),
                    text = " ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = suggestion,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                GrayCircleWithNumber2(count, color)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ButtonEvents(viewModel)
            }
        }

        // Watermark text at the bottom center
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = poweredBy,
                color = Color.LightGray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Light
            )
        }
    }

}

@Composable
private fun getChantFeedback(chantFeedback: ChantViewModel.ChantFeedback) : String {
    return  when (chantFeedback) {
        is ChantViewModel.ChantFeedback.Begin -> stringResource(id = R.string.chant_feedback_begin)
        is ChantViewModel.ChantFeedback.VeryFast -> stringResource(id = R.string.chant_feedback_very_fast)
        is ChantViewModel.ChantFeedback.Fast -> stringResource(id = R.string.chant_feedback_fast)
        is ChantViewModel.ChantFeedback.Good -> stringResource(id = R.string.chant_feedback_good)
        is ChantViewModel.ChantFeedback.SlowThanUsual -> stringResource(id = R.string.chant_feedback_slow_than_usual)
        is ChantViewModel.ChantFeedback.Slow -> stringResource(id = R.string.chant_feedback_slow)
        is ChantViewModel.ChantFeedback.VerySlow -> stringResource(id = R.string.chant_feedback_very_slow)
    }
}

@Composable
fun ButtonEvents(viewModel: ChantViewModel) {
    val decrementText = stringResource(id = R.string.decrement_button)
    val incrementText = stringResource(id = R.string.increment_button)
    // Create a FocusRequester
    val focusRequester = FocusRequester()

    // Use LaunchedEffect to request focus
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Button(onClick = {
        viewModel.decrementCount()
    }) {
        Text(text = decrementText, fontSize = 34.sp, fontWeight = FontWeight.Bold)
    }

    // Apply focusRequester to the right button
    Button(
        onClick = {
            viewModel.incrementCount()
        },
        modifier = Modifier.focusRequester(focusRequester)
    ) {
        Text(text = incrementText, fontSize = 34.sp, fontWeight = FontWeight.Bold)
    }
}




@Composable
private fun ChantLogItem(log: ChantLog) {
    Text(
        text = "Mala: ${log.malaNumber}, Time: ${
            String.format(Locale.US,
                "%.2f",
                log.timeConsumed / (60 * 1000.0)
            )
        } min",
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp)
    )
}





