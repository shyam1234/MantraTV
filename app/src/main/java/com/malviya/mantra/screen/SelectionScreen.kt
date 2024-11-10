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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.ChantViewModel
import java.util.Locale


// Data class to hold mala number and time consumed
data class ChantLog(val malaNumber: Int,
                    val timeConsumed: Long,
                    val totalTime : Long)


@Composable
fun GreetingScreen(name : String, viewModel: ChantViewModel) {
    val count by viewModel.count.collectAsState()

    val color by viewModel.color.collectAsState()

    val suggestion by viewModel.chantFeedback.collectAsState()

    val chantLogs by viewModel.chantLogs.collectAsState()

    val oneBeadTimeForRendering by viewModel.oneBeadTimeForRendering.collectAsState()

    val malaNumber by viewModel.malaNumber.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // Enables vertical scrolling
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MantraRender(name)

            Spacer(modifier = Modifier.weight(1f)) // Takes up remaining space

            if (chantLogs.isNotEmpty()) {
                val totalTime = viewModel.convertMillisToReadableTime(chantLogs.last().totalTime)
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp),
                    text = "Sampurnamala: ${chantLogs.last().malaNumber}/$totalTime",
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
                ButtonEvents(viewModel, chantLogs)
            }
        }

        // Watermark text at the bottom center
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Brought to you by Malviya Technologies",
                color = Color.LightGray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Light
            )
        }
    }

}

@Composable
fun ButtonEvents(viewModel: ChantViewModel, chantLogs: List<ChantLog>) {
    // Create a FocusRequester
    val focusRequester = FocusRequester()

    // Use LaunchedEffect to request focus
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Button(onClick = {
        viewModel.decrementCount()
    }) {
        Text(text = "-1", fontSize = 34.sp, fontWeight = FontWeight.Bold)
    }

    // Apply focusRequester to the right button
    Button(
        onClick = {
            viewModel.incrementCount()
        },
        modifier = Modifier.focusRequester(focusRequester)
    ) {
        Text(text = "+1", fontSize = 34.sp, fontWeight = FontWeight.Bold)
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





