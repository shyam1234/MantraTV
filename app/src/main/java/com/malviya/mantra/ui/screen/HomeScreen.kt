package com.malviya.mantra.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.malviya.mantra.ui.components.GrayCircleWithNumber2
import com.malviya.mantra.ui.components.MantraScreenLayout
import com.malviya.mantra.ui.viewmodel.ChantViewModel

/**
 * Data class representing a completed mala round with timing information
 * 
 * @param malaNumber The sequential number of the completed mala
 * @param timeConsumed Time taken to complete this specific mala round
 * @param totalTime Cumulative time spent chanting across all mala rounds
 */
data class ChantLog(
    val malaNumber: Int,
    val timeConsumed: Long,
    val totalTime: Long
)

/**
 * Main counter screen for mantra chanting
 * 
 * @param name The mantra text to display
 * @param viewModel ChantViewModel for state management and business logic
 * 
 * Displays the traditional counter interface with:
 * - Mantra text at the top
 * - Circular counter with bead indicators in the center
 * - Control buttons (decrement, auto-chant, increment)
 * - Mala completion logs and feedback
 * - Flash messages and watermark
 */
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





