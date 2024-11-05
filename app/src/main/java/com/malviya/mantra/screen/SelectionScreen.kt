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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malviya.mantra.ui.ChantViewModel


const val ONE_MALA_ROUND_COUNT: Int = 108

// Data class to hold mala number and time consumed
data class ChantLog(val malaNumber: Int,
                    val timeConsumed: Long,
                    val totalTime : Long)


@Composable
fun GreetingScreen(name : String, viewModel: ChantViewModel) {
    // State to hold the list of mala numbers and times
//    var malaNumber by remember { mutableIntStateOf(1) }
//    var chantLogs by remember { mutableStateOf(listOf<ChantLog>()) }
//    var startTime by remember { mutableLongStateOf(0L) }
//    var count by remember { mutableIntStateOf(0) }
//    val malaNumber by viewModel.malaNumber.collectAsState()

    val count by viewModel.count.collectAsState()

    val color by viewModel.color.collectAsState()

    val suggestion by viewModel.chantFeedback.collectAsState()

    val chantLogs by viewModel.chantLogs.collectAsState()

    val oneBeadTimeForRendering by viewModel.oneBeadTimeForRendering.collectAsState()

    val malaNumber by viewModel.malaNumber.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MantraRender(name)

        Spacer(modifier = Modifier.weight(1f)) // Takes up remaining space


        // Display chant logs (Mala Number and Time consumed)
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.Top
//        ) {
//            items(chantLogs) { log ->
//                ChantLogItem(log)
//            }
//        }

        if(chantLogs.isNotEmpty()) {
            val totalTime = viewModel.convertMillisToReadableTime(chantLogs.last().totalTime)//String.format("%.2f", (chantLogs.last().totalTime / (60 * 1000.0).toFloat()))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Sampurnamala: ${chantLogs.last().malaNumber}/$totalTime",
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
            GrayCircleWithNumber(count, color)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ButtonEvents(viewModel,chantLogs)
        }
    }

}

@Composable
fun ButtonEvents(viewModel: ChantViewModel, chantLogs: List<ChantLog>) {
    Button(onClick = {
        // if (count > 1) count--
        viewModel.decrementCount()
        //chantLogs = chantLogs.dropLast(1) // Remove last entry when decrementing
    }) {
        Text(text = "-1", fontSize = 34.sp, fontWeight = FontWeight.Bold)
    }

    Button(onClick = {
        viewModel.incrementCount()
    }) {
        Text(text = "+1", fontSize = 34.sp, fontWeight = FontWeight.Bold)
    }


}



@Composable
private fun ChantLogItem(log: ChantLog) {
    Text(
        text = "Mala: ${log.malaNumber}, Time: ${
            String.format(
                "%.2f",
                log.timeConsumed / (60 * 1000.0)
            )
        } min",
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp)
    )
}





