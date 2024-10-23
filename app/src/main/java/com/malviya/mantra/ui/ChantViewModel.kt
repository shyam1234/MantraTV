package com.malviya.mantra.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malviya.mantra.screen.ChantLog
import com.malviya.mantra.screen.ONE_MALA_ROUND_COUNT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChantViewModel : ViewModel() {

   private val _chantLogs = MutableStateFlow(listOf<ChantLog>())
    val chantLogs: StateFlow<List<ChantLog>> = _chantLogs

    private val _malaNumber = MutableStateFlow<Int>(0)
    val malaNumber: StateFlow<Int> = _malaNumber

    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count

    private var startTime = System.currentTimeMillis()
    private var totalTime = 0L

    fun incrementCount() {
        viewModelScope.launch {
            if (_count.value == ONE_MALA_ROUND_COUNT) {
                // Mala round is completed, reset count and log time
                _count.value = 0
                _malaNumber.value += 1

                // Calculate time taken for the current mala and log it
                val timeDiff = System.currentTimeMillis() - startTime
                totalTime += timeDiff
                _chantLogs.value += ChantLog(_malaNumber.value, timeDiff,totalTime )

                // Reset start time for the next mala
                startTime = System.currentTimeMillis()
            } else {
                // Start time when count is 1 (first bead in mala)
                if (_count.value == 0) {
                    startTime = System.currentTimeMillis()
                }
                // Increment the bead count
                _count.value += 1
            }
        }
    }


    fun decrementCount() {
        if (_count.value > 0) {
            _count.value -= 1
        }
    }

}