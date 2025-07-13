package com.malviya.mantra.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.malviya.mantra.ui.components.GrayCircleWithNumber2
import com.malviya.mantra.ui.components.MantraScreenLayout
import com.malviya.mantra.ui.viewmodel.ChantViewModel

// Data class to hold mala number and time consumed
data class ChantLog(
    val malaNumber: Int,
    val timeConsumed: Long,
    val totalTime: Long
)

@Composable
fun GreetingScreen(name: String, viewModel: ChantViewModel) {
    val remoteConfigService by viewModel.remoteConfigService.collectAsState()
    val count by viewModel.count.collectAsState()
    val color by viewModel.color.collectAsState()

    MantraScreenLayout(
        name = name,
        viewModel = viewModel,
        flashMessage = remoteConfigService.getFlashMessage(),
        centerContent = {
            GrayCircleWithNumber2(count, color)
        },
        onIncrement = { viewModel.incrementCount(true) },
        onDecrement = { viewModel.decrementCount(true) }
    )
}





