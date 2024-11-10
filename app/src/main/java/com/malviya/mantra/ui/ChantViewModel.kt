package com.malviya.mantra.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malviya.mantra.screen.ChantLog
import com.malviya.mantra.ui.theme.Yellow40
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

const val ONE_MALA_ROUND_COUNT: Int = 10
const val IDLE_TIME_FOR_ONE_BEAD: Long = 4000

class ChantViewModel : ViewModel() {

   private val _chantLogs = MutableStateFlow(listOf<ChantLog>())
    val chantLogs: StateFlow<List<ChantLog>> = _chantLogs

    private val _malaNumber = MutableStateFlow<Int>(0)
    val malaNumber: StateFlow<Int> = _malaNumber

    private val _oneBeadTimeForRendering = MutableStateFlow<Long>(0)
    val oneBeadTimeForRendering: StateFlow<Long> = _oneBeadTimeForRendering

    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count

    private val _color = MutableStateFlow<Color>(Color.Gray)
    val color : StateFlow<Color> = _color

    private val _chantFeedback = MutableStateFlow<String>("Let's Begin")
    val chantFeedback : StateFlow<String> = _chantFeedback


    private var startTime = System.currentTimeMillis()
    private var oneBidTAT = System.currentTimeMillis()
    private var totalTime = 0L

    fun incrementCount() {
        viewModelScope.launch {
            if (_count.value == ONE_MALA_ROUND_COUNT) {
                // Mala round is completed, reset count and log time
                _count.value = 0
                _color.value = Color.Gray
                _chantFeedback.value = "Let's Begin"

            } else {
                // Start time when count is 1 (first bead in mala)
                if (_count.value == 0) {
                    startTime = System.currentTimeMillis()
                    _oneBeadTimeForRendering.value = IDLE_TIME_FOR_ONE_BEAD
                }else{
                    _oneBeadTimeForRendering.value = (System.currentTimeMillis() - oneBidTAT).toLong()
                }

                getCircleColor(_oneBeadTimeForRendering.asStateFlow().value)

                if (_count.value == ONE_MALA_ROUND_COUNT-1) {
                    // Calculate time taken for the current mala and log it
                    _malaNumber.value += 1
                    val malaCompletedTime = System.currentTimeMillis() - startTime
                    totalTime += malaCompletedTime
                    _chantLogs.value += ChantLog(_malaNumber.value, malaCompletedTime,totalTime )

                }
                // Increment the bead count
                oneBidTAT = System.currentTimeMillis()
                _count.value += 1


                //-----------------------
                Timber.d("one bead count: ${_count.value}, time: ${_oneBeadTimeForRendering.asStateFlow().value}")
            }
        }
    }


    fun decrementCount() {
        if (_count.value > 0) {
            _count.value -= 1
        }
    }

    fun getCircleColor(timeConsumedForOneBid: Long){
        when(timeConsumedForOneBid){
            in 0..2999 -> {
                _color.value = Color.Red
                _chantFeedback.value = "You are chanting very fast!"
            }  // very fast
            in 3000..3500 -> {
                _color.value = Color.Red
                _chantFeedback.value = "You are chanting little faster than usual!"
            }  // very fast
            in 3501..4900 -> {
                _color.value = Color.Green
                _chantFeedback.value = "You are doing good!"
            }  // good
            in 4901..6000 -> {
                _color.value = Yellow40
                _chantFeedback.value = "You are chanting little slow!"
            }
            in 6001..8000 -> {
                _color.value = Yellow40
                _chantFeedback.value = "You are chanting slow!"
            }
            else -> {
                _color.value = Yellow40
                _chantFeedback.value = "You are chanting very slow!"
            }   // very slow
        }
    }


    fun convertMillisToReadableTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return when {
            hours > 0 -> String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds)+" hr"  // Show hours, minutes, and seconds
            minutes > 0 -> String.format(Locale.US,"%02d:%02d", minutes, seconds)+" min" // Show minutes and seconds
            else -> String.format(Locale.US,"%02d", seconds)+" sec"  // Show only seconds
        }
    }



}